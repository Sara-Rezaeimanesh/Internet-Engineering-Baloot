package com.baloot.IE.repository.Comment;

import com.baloot.IE.domain.Comment.Comment;
import com.baloot.IE.repository.ConnectionPool;
import com.baloot.IE.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CommentRepository extends Repository<Comment, String> {
    private static CommentRepository instance;
    private static final String TABLE_NAME = "COMMENTS";

    public CommentRepository getInstance() throws SQLException {
        if(instance == null)
            instance = new CommentRepository();
        return instance;
    }

    private CommentRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(id CHAR(50),\nuserEmail CHAR(225),\ncommodityId CHAR(225),"  +
                                "\ntext CHAR VARYING(500),\n date DATE, likes INTEGER, dislikes INTEGER\nPRIMARY KEY(id));",
                        TABLE_NAME)
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement(String field_name) {
        return String.format("SELECT * FROM %s u WHERE u.%s = ?;", TABLE_NAME, field_name);
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, String id) throws SQLException {
        st.setString(1, id);
    }

    @Override
    protected String getInsertStatement() {
        return String.format("INSERT IGNORE INTO %s(id, userEmail, commodityId, text, date, likes, dislikes) VALUES(?,?,?,?,?,?,?)", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Comment data) throws SQLException {
        Object[] values = new Object[]{data.getId(), data.getUserEmail(),
                                        data.getCommodityId(), data.getText(),
                                        data.getDate(), data.getLikes(),
                                        data.getDislikes()};

        for (int i = 0; i < values.length; i++) {
            st.setObject(i + 1, values[i]);
        }
    }

    @Override
    protected String getFindAllStatement(String searchString) {
        return null;
    }

    @Override
    protected Comment convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected ArrayList<Comment> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected String getUpdateStatement(String varName, String newValue, String whereField, String whereValue) {
        return null;
    }
}
