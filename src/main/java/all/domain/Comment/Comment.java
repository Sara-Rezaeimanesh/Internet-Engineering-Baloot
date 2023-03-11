package all.domain.Comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import springfox.documentation.spring.web.json.Json;

public class Comment {
    private String userEmail;
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
    }
    public void incLike(int incVal) { likes += incVal; }
    public void incDislike(int incVal) { dislikes += incVal; }

    public int getCommodityId() {
        return commodityId;
    }
}
