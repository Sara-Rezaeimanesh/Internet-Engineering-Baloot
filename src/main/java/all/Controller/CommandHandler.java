package all.Controller;

import all.domain.Amazon.Amazon;
import all.domain.Rating.Rating;
import all.domain.Supplier.Supplier;
import all.domain.User.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import org.apache.commons.io.FileUtils;
import org.springframework.data.jpa.repository.query.EscapeCharacter;
import com.google.common.io.Resources;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    public void run(int port) throws Exception {
        var app = Javalin.create(/*config*/)
                .get("/", ctx -> ctx.result("Baloot Application"))
                .start(port);

        app.get("/commodities", ctx -> {
            try {
                ctx.html(amazon.createCommoditiesPage());
            } catch (Exception e){
                System.out.println(e.getMessage());
                ctx.status(502);
            }
        });
        app.get("/commodities/:id", ctx -> {
            try {
                ctx.html(amazon.createCommodityPage(ctx.pathParam("id")));
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
        app.post("/addCredit/:id/:credit", ctx -> {
            try {
                String userId = ctx.pathParam("id");
                String credit = ctx.pathParam("credit");
                amazon.increaseCredit(userId, Integer.parseInt(credit));
                ctx.redirect("/users/" + userId);
            } catch (Exception e){
                if(Objects.equals(e.getMessage(), USER_DOES_NOT_EXIST_ERROR))
                    ctx.status(404);
                else
                    ctx.status(502);
            }

        });
        app.post("/addCredit/:id", ctx -> {
            String credit = ctx.formParam("credit");
            String userId = ctx.pathParam("id");
            ctx.redirect("/addCredit/" + userId + "/"+ credit );
        });
        app.post("/addToBuyList/:id", ctx -> {
            String username = ctx.formParam("user_id");
            String commodityId = ctx.pathParam("id");
            ctx.redirect("/addToBuyList/"+username+"/"+commodityId);
        });
        app.get("/addToBuyList/:username/:id", ctx -> {
            try {
                String username = ctx.pathParam("username");
                String commodityId = ctx.pathParam("id");
                amazon.addToBuyList(username, Integer.parseInt(commodityId));
                ctx.redirect("/commodities/"+commodityId);

            } catch (Exception e){
                if(Objects.equals(e.getMessage(), USER_DOES_NOT_EXIST_ERROR)   ||
                   Objects.equals(e.getMessage(), PRODUCT_HAS_BOUGHT_ERROR)    ||
                   Objects.equals(e.getMessage(), PRODUCT_DOES_NOT_EXIT_ERROR) ||
                   Objects.equals(e.getMessage(), PRODUCT_DOES_NOT_EXIT_ERROR))
                    ctx.status(404);
                else
                    ctx.status(502);
            }
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
                        Objects.equals(e.getMessage(), PRODUCT_HAS_BOUGHT_ERROR) ||
                        Objects.equals(e.getMessage(), PRODUCT_HAS_NOT_BOUGHT_ERROR))
                    ctx.status(404);
                else
                ctx.status(502);
            }
        });
//        app.get("/voteComment/:username/:cid/:vote", ctx -> {
//            try {
//                String username = ctx.pathParam("username");
//                String commentId = ctx.pathParam("cid");
//                String vote = ctx.pathParam("vote");
//                String stat = amazon.voteComment(commentId, vote);
//                if(!Objects.equals(stat, "success"))
//                    ctx.html(stat);
//                else
//                    ctx.redirect("/commodities/"+commodityId);
//            } catch (Exception e){
//                System.out.println(e.getMessage());
//                ctx.status(502);
//            }
//        });
//        app.post("/voteComment/:cid/:vote", ctx -> {
//            String username = ctx.formParam("username");
//            String commentId = ctx.pathParam("cid");
//            String vote = ctx.pathParam("vote");
//            ctx.redirect("/voteComment/"+username+"/"+commentId+"/"+vote);
//        });
//        app.get("/commodities/search/:categories", ctx -> {
//            try {
//                String start_price = ctx.pathParam("start_price");
//                String end_price = ctx.pathParam("end_price");
//                String commodityId = ctx.pathParam("commodityId");
//                String stat = amazon.filterCOmmoditiesByCategory();
//                if(!Objects.equals(stat, "success"))
//                    ctx.html(stat);
//                else
//                    ctx.redirect("/commodities/"+commodityId);
//            } catch (Exception e){
//                System.out.println(e.getMessage());
//                ctx.status(502);
//            }
//        });

    }
}
