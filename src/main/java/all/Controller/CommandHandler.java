package all.Controller;

import all.domain.Amazon.Amazon;
import all.domain.Supplier.Supplier;
import all.domain.User.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import org.springframework.data.jpa.repository.query.EscapeCharacter;

public class CommandHandler {
    private final int COMMAND_IDX = 0;
    private final int ARGS_IDX = 1;

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

    private String generateCoursesPage() throws Exception {
        String coursesHTML = readHTMLPage("Commodities.html");
        List<Offering> offerings = amazon.get();
        String courseItemString = readHTMLPage("courses_item.html");

        for (Offering offering : offerings) {
            HashMap<String, String> courseContent = new HashMap<>();
            courseContent.put("code", offering.getCourseCode());
            courseContent.put("classCode", offering.getClassCode());
            courseContent.put("name", offering.getName());
            courseContent.put("units", Integer.toString(offering.getUnits()));
            courseContent.put("capacity", Integer.toString(offering.getCapacity()));
            courseContent.put("type", offering.getType());
            courseContent.put("classDays", offering.getClassDayString("|"));
            courseContent.put("classTime", offering.getClassTime().getTime());
            courseContent.put("examTimeStart", offering.getExamTime().getStart());
            courseContent.put("examTimeEnd", offering.getExamTime().getEnd());
            courseContent.put("prerequisites", offering.getPrerequisitesString());
            coursesHTML += HTMLHandler.fillTemplate(courseItemString, courseContent);
        }
        coursesHTML += readHTMLPage("courses_end.html");
        return coursesHTML;
    }


    public void run(int port) throws Exception {
        var app = Javalin.create(/*config*/)
                .get("/", ctx -> ctx.result("Baloot Application"))
                .start(port);

        app.get("/commodities", ctx -> {
            try {
                ctx.html(generateCoursesPage());
            } catch (Exception e){
                System.out.println(e.getMessage());
                ctx.status(502);
            }
        });
        app.get("/course/:code/:classCode", ctx -> {
            try {
                ctx.html(generateCoursePage(ctx.pathParam("code"), ctx.pathParam("classCode")));
            } catch (BolbolestanCourseNotFoundError e) {
                ctx.html(readHTMLPage("404.html"));
            } catch (Exception e){
                System.out.println(e.getMessage());
                ctx.status(502).result(Integer.toString(ctx.status()) + ":| " + e.getMessage());
            }
        });
    }
}
