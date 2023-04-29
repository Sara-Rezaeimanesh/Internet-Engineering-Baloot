package com.baloot.IE.domain.Comment;

import com.baloot.IE.domain.CommentVote.CommentVote;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class Comment {
    private static final AtomicInteger count = new AtomicInteger(0);
    private int id;
    private final String userEmail;
    private final int commodityId;
    private final String text;
    private final String date;
    @JsonIgnore
    private final ArrayList<CommentVote> votes;
    private int likes;
    private int dislikes;

    public void initialize() {
        this.id = count.incrementAndGet();
    }

    public Comment(String userEmail, int commodityId, String text, String date) {
        this.userEmail = userEmail;
        this.commodityId = commodityId;
        this.text = text;
        this.date = date;
        votes = new ArrayList<>();
    }

    public boolean idMatches(int commentId) { return commentId == id; }

    public void updateVote(String userEmail, int vote) {
        boolean exists = false;
        for(CommentVote cv : votes)
            if(Objects.equals(cv.getUserEmail(), userEmail)) {
                exists = true;
                int previous_vote = cv.getVote();
                cv.updateVote(vote);
                if(previous_vote == 1)
                    likes -= 1;
                else if(previous_vote == -1)
                    dislikes -= 1;
            }
        if(!exists)
            votes.add(new CommentVote(userEmail, vote));
        if(vote == 1)
            likes += 1;
        else if(vote == -1)
            dislikes += 1;

    }
}
