package all.Controller;

import all.domain.Amazon.Amazon;
import all.domain.Supplier.Supplier;
import all.domain.User.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import org.apache.commons.io.FileUtils;
import org.springframework.data.jpa.repository.query.EscapeCharacter;
import com.google.common.io.Resources;

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
//        app.get("/course/:code/:classCode", ctx -> {
//            try {
//                ctx.html(generateCoursePage(ctx.pathParam("code"), ctx.pathParam("classCode")));
//            } catch (BolbolestanCourseNotFoundError e) {
//                ctx.html(readHTMLPage("404.html"));
//            } catch (Exception e){
//                System.out.println(e.getMessage());
//                ctx.status(502).result(Integer.toString(ctx.status()) + ":| " + e.getMessage());
//            }
//        });
    }
}
