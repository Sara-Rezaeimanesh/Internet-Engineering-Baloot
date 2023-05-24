package com.baloot.IE.domain.CommentVote;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentVote {
    private String userEmail;
    private int vote;
    private int commentId;

    public void updateVote(int newVote) {
        if(vote != 1 && vote != -1 && vote != 0)
            throw new IllegalArgumentException("Invalid comment code. Please use 1, -1 or 0.");
        vote = newVote;
    }
}
