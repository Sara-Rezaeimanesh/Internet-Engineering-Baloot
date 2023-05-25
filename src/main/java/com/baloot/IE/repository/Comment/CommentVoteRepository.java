package com.baloot.IE.repository.Comment;

import com.baloot.IE.domain.Comment.Comment;
import com.baloot.IE.domain.CommentVote.CommentVote;
import com.baloot.IE.repository.ConnectionPool;
import com.baloot.IE.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CommentVoteRepository extends Repository<CommentVote, String> {
    private static CommentVoteRepository instance;
    private static final String TABLE_NAME = "COMMENT_VOTES";

    public static CommentVoteRepository getInstance() throws SQLException {
        if(instance == null)
            instance = new CommentVoteRepository();
        return instance;
    }

    private CommentVoteRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(userEmail CHAR(225),\nvote CHAR(225),\ncommentId MEDIUMINT,"  +
                                "PRIMARY KEY(userEmail, commentId),\n"+
                                "foreign key (userEmail) references USERS(email),\n" +
                                "foreign key (commentId) references COMMENTS(id));",
                        TABLE_NAME)
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement(String field_name) {
        return String.format("SELECT * FROM %s u WHERE "+field_name+";", TABLE_NAME);
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, String id) throws SQLException {
        st.setString(1, id);
    }

    @Override
    protected String getInsertStatement() {
        return String.format("INSERT IGNORE INTO %s(userEmail, vote, commentId) VALUES(?,?,?)", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, CommentVote data) throws SQLException {
        Object[] values = new Object[]{data.getUserEmail(), data.getVote(), data.getCommentId()};

        for (int i = 0; i < values.length; i++) {
            st.setObject(i + 1, values[i]);
        }
    }

    @Override
    protected String getFindAllStatement(String searchString) {
        return String.format("SELECT * FROM %s WHERE %s;", TABLE_NAME, searchString);
    }

    @Override
    protected CommentVote convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new CommentVote(rs.getString(1), Integer.parseInt(rs.getString(2)),
                                Integer.parseInt(rs.getString(3)));
    }

    @Override
    protected ArrayList<CommentVote> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<CommentVote> commentVotes = new ArrayList<>();
        while (rs.next()) {
            commentVotes.add(this.convertResultSetToDomainModel(rs));
        }
        return commentVotes;
    }

    @Override
    protected String getUpdateStatement(String varName, String newValue, String whereField, String whereValue) {
        return String.format("update %s set %s = %s where %s;",
                TABLE_NAME, varName, newValue, whereValue);
    }
}
