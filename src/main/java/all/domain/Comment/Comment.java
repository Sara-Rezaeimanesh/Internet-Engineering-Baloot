package all.domain.Comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import springfox.documentation.spring.web.json.Json;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Comment {
    private static final AtomicInteger count = new AtomicInteger(0);
    private int id;
    private String userEmail;
    private String userId;
    private int commodityId;
    private String text;
    private String date;

    @JsonIgnore
    private int likes;
    @JsonIgnore
    private int dislikes;

    public Comment(String userEmail, int commodityId, String text, String date) {
        this.userEmail = userEmail;
        this.commodityId = commodityId;
        this.text = text;
        this.date = date;
        this.id = count.incrementAndGet();
    }

    public void initialize() {
        this.id = count.incrementAndGet();
    }

    public int getCommodityId() {
        return commodityId;
    }

    public boolean idMatches(int commentId) { return commentId == id; }

    public void updateVote(int vote) {
        if(vote == 1)
            likes += 1;
        else
            dislikes += 1;
    }

    public String createHTML() {
        return
                "    <tr>\n" +
                "        <td>" + userId + "</td>\n" +
                "        <td>" + text + "</td>\n" +
                "        <td>" + date + "</td>\n" +
                "        <td>\n" +
                "            <form action=\"/voteComment/" + id + "/1\" method=\"POST\">\n" +
                "                <label for=\"\">" + likes + "</label>\n" +
                "                <input\n" +
                "                        id=\"form_comment_id\"\n" +
                "                        type=\"hidden\"\n" +
                "                        name=\"comment_id\"\n" +
                "                        value=\"01\"\n" +
                "                />\n" +
                "                <button type=\"submit\">like</button>\n" +
                "            </form>\n" +
                "        </td>\n" +
                "        <td>\n" +
                "            <form action=\"/voteComment/" + id + "/-1\" method=\"POST\">\n" +
                "                <label for=\"\">" + dislikes + "</label>\n" +
                "                <input\n" +
                "                        id=\"form_comment_id\"\n" +
                "                        type=\"hidden\"\n" +
                "                        name=\"comment_id\"\n" +
                "                        value=\"01\"\n" +
                "                />\n" +
                "                <button type=\"submit\">dislike</button>\n" +
                "            </form>\n" +
                "        </td>\n" +
                "    </tr>";
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void updateUserId(String username) {
        userId = username;
    }
}
