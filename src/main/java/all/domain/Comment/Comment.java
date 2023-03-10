package all.domain.Comment;

public class Comment {
    String userEmail;
    int commodityId;
    String text;
    String date;

    public Comment(String userEmail, int commodityId, String text, String date) {
        this.userEmail = userEmail;
        this.commodityId = commodityId;
        this.text = text;
        this.date = date;
    }
}
