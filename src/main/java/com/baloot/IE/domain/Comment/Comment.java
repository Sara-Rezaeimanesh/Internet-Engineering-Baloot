package com.baloot.IE.domain.Comment;

import com.baloot.IE.domain.CommentVote.CommentVote;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Comment {
    private static final AtomicInteger count = new AtomicInteger(0);
    private int id;
    private String userEmail;
    private String userId;
    private int commodityId;
    private String text;
    private String date;
    private ArrayList<CommentVote> votes;

    @JsonIgnore
    private int likes;
    @JsonIgnore
    private int dislikes;

    public void initialize() {
        this.id = count.incrementAndGet();
    }

    public Comment(String userEmail, int commodityId, String text, String date) {
        this.userEmail = userEmail;
        this.commodityId = commodityId;
        this.text = text;
        this.date = date;
    }

    public int getCommodityId() {
        return commodityId;
    }

    public boolean idMatches(int commentId) { return commentId == id; }

    public void updateVote(String userId, int vote) {
        for(CommentVote cv : votes)
            if(Objects.equals(cv.getUsername(), userId)) {
                int previous_vote = cv.getVote();
                cv.updateVote(vote);
                likes += vote - previous_vote;
                dislikes -= vote - previous_vote;
            }
        votes.add(new CommentVote(userId, vote));
    }

    public String createHTML() {
        return
                "    <tr>\n" +
                "        <td>" + userEmail + "</td>\n" +
                "        <td>" + text + "</td>\n" +
                "        <td>" + date + "</td>\n" +
                "        <td>\n" +
                "            <form action=\"like\" method=\"POST\">\n" +
                "                <label for=\"\">" + likes + "</label>\n" +
                "                <input\n" +
                "                        id=\"form_comment_id\"\n" +
                "                        type=\"hidden\"\n" +
                "                        name=\"comment_id\"\n" +
                "                        value=\""+ id +"\"\n" +
                "                />\n" +
                "                <button type=\"submit\" name=\"action\" value=\"like\">like</button>\n" +
                "            </form>\n" +
                "        </td>\n" +
                "        <td>\n" +
                "            <form action=\"dislike\" method=\"POST\">\n" +
                "                <label for=\"\">" + dislikes + "</label>\n" +
                "                <input\n" +
                "                        id=\"form_comment_id\"\n" +
                "                        type=\"hidden\"\n" +
                "                        name=\"comment_id\"\n" +
                "                        value=\""+ id +"\"\n" +
                "                />\n" +
                "                <button type=\"submit\" name=\"action\" value=\"dislike\">dislike</button>\n" +
                "            </form>\n" +
                "        </td>\n" +
                "    </tr>";
    }

    public String getUserEmail() {
        return userEmail;
    }

}
