package all.Controller;

import all.domain.Amazon.Amazon;
import all.domain.Product.Product;
import all.domain.Rating.Rating;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.javalin.Javalin;
import org.apache.commons.io.FileUtils;

public class CommandHandler {
    private final int COMMAND_IDX = 0;
    private final int ARGS_IDX = 1;

    private static final String PRODUCT_HAS_BOUGHT_ERROR = "Product already bought!";
    private static final String PRODUCT_HAS_NOT_BOUGHT_ERROR = "Product hasn't already bought!";
    private final String PRODUCT_ALREADY_EXIST_ERROR = "Product already exista!";
    private final String SUPPLIER_DOES_NOT_EXIST_ERROR = "Supplier is not exist!";
    private final String PRODUCT_DOES_NOT_EXIT_ERROR = "Product does not exist!";
    private final String USER_DOES_NOT_EXIST_ERROR = "User does not exist!";
    private static final String PRODUCT_IS_NOT_IN_STOCK = "Product is not in stock!";

    static Amazon amazon;
    public static ObjectMapper mapper;

    public CommandHandler(Amazon amazon) {
        CommandHandler.amazon = amazon;
        mapper = new ObjectMapper();
    }

    private String readHTMLPage(String fileName) throws Exception {
        File file = new File(Resources.getResource("templates/" + fileName).toURI());
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    public void run(int port) throws Exception {
        var app = Javalin.create(/*config*/)
                .get("/", ctx -> ctx.result("Baloot Application"))
                .start(port);

        app.get("/commodities", ctx -> {
            try {
                ctx.html(amazon.createCommoditiesPage(new ArrayList<Product>(), "all"));
            } catch (Exception e){
                System.out.println(e.getMessage());
                ctx.status(502);
            }
        });
        app.get("/commodities/:id", ctx -> {
            try {
                ctx.html(amazon.createCommodityPage(Integer.parseInt(ctx.pathParam("id"))));
            } catch (Exception e){
                System.out.println(e.getMessage());
                ctx.status(502);

            }
        });
        app.get("/providers/:id", ctx -> {
            try {
                System.out.println(1);
                ctx.html(amazon.createProviderPage(ctx.pathParam("id")));
            } catch (Exception e){
                System.out.println(e.getMessage());
                ctx.status(502);

            }
        });
        app.get("/users/:id", ctx -> {
            try {
                System.out.println(1);
                ctx.html(amazon.createUserPage(ctx.pathParam("id")));
            } catch (Exception e){
                System.out.println(e.getMessage());
                ctx.status(502);
            }
        });
        app.get("/addCredit/:id/:credit", ctx -> {
            try {
                String userId = ctx.pathParam("id");
                String credit = ctx.pathParam("credit");
                System.out.println("hi" + userId + " " + credit);
                amazon.increaseCredit(userId, Integer.parseInt(credit));
                ctx.redirect("/users/" + userId);
            } catch (Exception e){
                System.out.println(e.getMessage());
                if(Objects.equals(e.getMessage(), USER_DOES_NOT_EXIST_ERROR))
                    ctx.html(readHTMLPage("404.html"));
                else
                    ctx.status(502);
            }

        });
        app.post("/addCredit/:id", ctx -> {
            String userId = ctx.pathParam("id");
            String credit = ctx.formParam("credit");
            ctx.redirect("/addCredit/" + userId + "/"+ credit );
        });
        app.post("/addToBuyList/:id", ctx -> {
            String username = ctx.formParam("user_id");
            String commodityId = ctx.pathParam("id");
            ctx.redirect("/addToBuyList/"+username+"/"+commodityId);
        });
//        app.get("/addToBuyList/:username/:id", ctx -> {
//            try {
//                String username = ctx.pathParam("username");
//                String commodityId = ctx.pathParam("id");
//                amazon.addToBuyList(username, Integer.parseInt(commodityId));
//                ctx.redirect("/commodities/"+commodityId);
//
//            } catch (Exception e){
//                if(Objects.equals(e.getMessage(), USER_DOES_NOT_EXIST_ERROR)   ||
//                   Objects.equals(e.getMessage(), PRODUCT_DOES_NOT_EXIT_ERROR))
//                    ctx.html(readHTMLPage("404.html"));
//                else if(Objects.equals(e.getMessage(), PRODUCT_HAS_BOUGHT_ERROR))
//                    ctx.html(readHTMLPage("403.html"));
//                else
//                    ctx.status(502);
//            }
//        });
        app.get("/removeFromBuyList/:username/:id", ctx -> {
            try {
                String username = ctx.pathParam("username");
                String commodityId = ctx.pathParam("id");
                amazon.removeFromBuyList(username, Integer.parseInt(commodityId));
                ctx.redirect("/users/" + username);
            } catch (Exception e){
                if(Objects.equals(e.getMessage(), USER_DOES_NOT_EXIST_ERROR)   ||
                        Objects.equals(e.getMessage(), PRODUCT_DOES_NOT_EXIT_ERROR))
                    ctx.html(readHTMLPage("404.html"));
                else if(Objects.equals(e.getMessage(), PRODUCT_HAS_NOT_BOUGHT_ERROR))
                    ctx.html(readHTMLPage("403.html"));
                else
                    ctx.status(502);
            }
        });
        app.post("/removeFromBuyList/:username/:id", ctx -> {
            String username = ctx.pathParam("username");
            String commodityId = ctx.pathParam("id");
            amazon.removeFromBuyList(username, Integer.parseInt(commodityId));
            ctx.redirect("/removeFromBuyList/" + username + "/" + commodityId);
        });

        app.post("/rateCommodity/:id", ctx -> {
            String username = ctx.formParam("user_id");
            String quantity = ctx.formParam("quantity");
            String commodityId = ctx.pathParam("id");
            ctx.redirect("/rateCommodity/"+username+"/"+commodityId+"/"+quantity);
        });
        app.get("/rateCommodity/:username/:cid/:rate", ctx -> {
            try {
                String username = ctx.pathParam("username");
                String quantity = ctx.pathParam("rate");
                String commodityId = ctx.pathParam("cid");
                Rating rating = new Rating(username, Integer.parseInt(commodityId), Integer.parseInt(quantity));
                amazon.rateCommodity(rating);
                ctx.redirect("/commodities/"+commodityId);
            } catch (Exception e){
                if(Objects.equals(e.getMessage(), USER_DOES_NOT_EXIST_ERROR) ||
                        Objects.equals(e.getMessage(), PRODUCT_DOES_NOT_EXIT_ERROR))
                    ctx.html(readHTMLPage("404.html"));
                else if(Objects.equals(e.getMessage(), PRODUCT_HAS_BOUGHT_ERROR))
                    ctx.html(readHTMLPage("403.html"));
                else
                    ctx.status(502);
            }
        });
        app.get("/voteComment/:username/:cid/:vote", ctx -> {
            try {

                String username = ctx.pathParam("username");
                String commentId = ctx.pathParam("cid");
                String vote = ctx.pathParam("vote");
                System.out.println("hello "+commentId+" "+vote);
                if(!vote.equals("0") && !vote.equals("1") && !vote.equals("-1"))
                    throw new Exception("Invalid Vote");
                amazon.voteComment(commentId, Integer.parseInt(vote));
                Product p = amazon.findCommentCommodity(commentId);
                ctx.redirect("/commodities/"+p.getId());

            } catch (Exception e){
                if(Objects.equals(e.getMessage(), USER_DOES_NOT_EXIST_ERROR) ||
                        Objects.equals(e.getMessage(), PRODUCT_DOES_NOT_EXIT_ERROR))
                    ctx.html(readHTMLPage("404.html"));
                else if(Objects.equals(e.getMessage(), "Invalid Vote"))
                    ctx.html(readHTMLPage("403.html"));
                else
                    ctx.status(502);
            }
        });
        app.post("/voteComment/:cid/:vote", ctx -> {
            String username = ctx.formParam("username");
            String commentId = ctx.pathParam("cid");
            String vote = ctx.pathParam("vote");
            System.out.println("hello "+commentId+" "+vote);
            ctx.redirect("/voteComment/"+username+"/"+commentId+"/"+vote);
        });
        app.get("/commodities/search/:categories", ctx -> {
            try {
                String category = ctx.pathParam("categories");
                System.out.println(category);
                ArrayList<Product> products = amazon.getCommoditiesByCategory(category);
                System.out.println(products.size());
                ctx.html(amazon.createCommoditiesPage(products, "category"));
            } catch (Exception e){
                    ctx.status(502);
            }
        });
        app.get("/commodities/search/:startPrice/:endPrice", ctx -> {
            try {
                int startPrice = Integer.parseInt(ctx.pathParam("startPrice"));
                int endPrice = Integer.parseInt(ctx.pathParam("endPrice"));
                ArrayList<Product> products = amazon.getCommodityByPriceInterval(startPrice, endPrice);
                ctx.html(amazon.createCommoditiesPage(products, "price"));
            } catch (Exception e){
                ctx.status(502);
            }
        });

    }
}
