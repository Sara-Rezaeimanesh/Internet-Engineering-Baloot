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
        app.post("/addToBuyList/:id", ctx -> {
            try {
                String username = ctx.formParam("user_id");
                String commodityId = ctx.pathParam("id");
                String stat = amazon.addToBuyList(username, Integer.parseInt(commodityId));
                if(!Objects.equals(stat, "success"))
                    ctx.html(stat);
                else
                    ctx.redirect("/commodities/"+commodityId);
            } catch (Exception e){
                System.out.println(e.getMessage());
                ctx.status(502);
            }
        });
        app.post("/rateCommodity/:id", ctx -> {
            try {
                String username = ctx.formParam("user_id");
                String quantity = ctx.formParam("quantity");
                String commodityId = ctx.pathParam("id");
                Rating rating = new Rating(username, Integer.parseInt(commodityId), Integer.parseInt(quantity));
                String stat = amazon.rateCommodity(rating);
                if(!Objects.equals(stat, "success"))
                    ctx.html(stat);
                else
                    ctx.redirect("/commodities/"+commodityId);
            } catch (Exception e){
                System.out.println(e.getMessage());
                ctx.status(502);
            }
        });

    }
}
