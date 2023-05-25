package com.baloot.IE.domain.Comment;

import com.baloot.IE.domain.CommentVote.CommentVote;
import com.baloot.IE.repository.Comment.CommentVoteRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class Comment {
    int id;
    private static final AtomicInteger count = new AtomicInteger(0);
    private final String userEmail;
    private final int commodityId;
    private final String text;
    private final String date;
    private int likes;
    private int dislikes;

    public Comment(String userEmail, int commodityId, String text, String date) {
        this.userEmail = userEmail;
        this.commodityId = commodityId;
        this.text = text;
        this.date = date;
    }

    public Comment(int id, String userEmail, int commodityId, String text, String date, int likes, int dislikes) {
        this.id = id;
        this.userEmail = userEmail;
        this.commodityId = commodityId;
        this.text = text;
        this.date = date;
        this.likes = likes;
        this.dislikes = dislikes;
    }
}
