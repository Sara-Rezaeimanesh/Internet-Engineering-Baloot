package com.baloot.IE.domain.Comment;

import com.baloot.IE.domain.CommentVote.CommentVote;
import com.baloot.IE.domain.Initializer.Initializer;
import com.baloot.IE.domain.Product.Product;
import com.baloot.IE.domain.User.User;
import com.baloot.IE.domain.User.UserManager;
import com.baloot.IE.repository.Comment.CommentRepository;
import com.baloot.IE.repository.Comment.CommentVoteRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CommentManager {
    private static CommentManager instance;
    private static final CommentRepository repository;

    static {
        try {
            repository = CommentRepository.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final CommentVoteRepository commentVoteRepository = CommentVoteRepository.getInstance();

    public static CommentManager getInstance() throws Exception {
        if(instance == null)
            instance = new CommentManager();
        return instance;
    }

    private CommentManager() throws Exception {
        Initializer initializer = new Initializer();
        ArrayList<Comment> comments = initializer.getCommentsFromAPI("comments");
        for(Comment c : comments) {
            repository.insert(c);
        }
    }

    public Comment findCommentById(String id) {
        Comment comment;
        try {
            comment = repository.findByField(id, "id");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(comment != null)
            return comment;
        throw new IllegalArgumentException("Comment does not exits.");
    }

    public boolean commentExists(String id) throws SQLException {
        Comment comment;
        comment = repository.findByField(id, "commentname");
        return comment != null;
    }

    public ArrayList<Comment> searchComments(String field, String field_name) {
        try{
            return repository.search(field, field_name);
        }
        catch (Exception e){
            return new ArrayList<>();
        }
    }

    public void addComment(Comment comment) throws Exception {
        repository.insert(comment);
    }

    public void voteComment(String userEmail, int commentId, int vote) throws SQLException {
        CommentVote commentVote = commentVoteRepository.findByField(new ArrayList<>(Arrays.asList(userEmail, String.valueOf(commentId))), "");
        Comment comment = repository.findByField(String.valueOf(commentId), "id");
        if(comment == null)
            throw new IllegalArgumentException("Comment does not exist.");

        int likes = comment.getLikes();
        int dislikes = comment.getDislikes();

        if(commentVote == null)
            commentVoteRepository.insert(new CommentVote(userEmail, vote, commentId));
        else {
            int previous_vote = commentVote.getVote();
            commentVoteRepository.update("vote", String.valueOf(vote),
                        "",new ArrayList<>(Arrays.asList(userEmail, String.valueOf(commentId))));
            if(previous_vote == 1)
                likes -= 1;
            else if(previous_vote == -1)
                dislikes -= 1;
        }
        if(vote == 1)
            likes += 1;
        else if(vote == -1)
            dislikes += 1;

        repository.update("likes", String.valueOf(likes), "id", String.valueOf(commentId));
        repository.update("dislikes", String.valueOf(dislikes), "id", String.valueOf(commentId));
    }
}
