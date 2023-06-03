package com.baloot.IE.repository.Product;

import com.baloot.IE.domain.CommentVote.CommentVote;
import com.baloot.IE.domain.Product.Category;
import com.baloot.IE.domain.Product.Product;
import com.baloot.IE.repository.Comment.CommentVoteRepository;
import com.baloot.IE.repository.ConnectionPool;
import com.baloot.IE.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryRepository extends Repository<Category, String>  {

    private static CategoryRepository instance;
    private static final String TABLE_NAME = "CATEGORIES";

    public static CategoryRepository getInstance() throws SQLException {
        if(instance == null)
            instance = new CategoryRepository();
        return instance;
    }

    private CategoryRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(productId CHAR(225),\ncategory CHAR(225),\n"  +
                                "PRIMARY KEY(productId, category),\n"+
                                "foreign key (productId) references PRODUCTS(id));",
                        TABLE_NAME)
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement(String field_name) {
        return null;
    }

    @Override
    protected String getSearchStatement(String field_name) {
        return String.format("SELECT * FROM %s WHERE productId = ?;", TABLE_NAME);
    }

    @Override
    protected void fillSearchValues(PreparedStatement st, String fields) throws SQLException {
        st.setInt(1, Integer.parseInt(fields));
    }

    @Override
    protected void fillUpdateValues(PreparedStatement st, String field, String where) throws SQLException {

    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, String id) throws SQLException {
    }

    @Override
    protected String getInsertStatement() {
        return String.format("INSERT IGNORE INTO %s(productId, category) VALUES(?,?)", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Category data) throws SQLException {
        Object[] values = new Object[]{data.getProductId(), data.getCategory()};

        for (int i = 0; i < values.length; i++) {
            st.setObject(i + 1, values[i]);
        }
    }

    @Override
    protected String getFindAllStatement() {
        return String.format("SELECT * FROM %s;", TABLE_NAME);
    }

    @Override
    protected Category convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new Category(Integer.parseInt(rs.getString(1)), rs.getString(2));
    }

    @Override
    protected ArrayList<Category> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<Category> commentVotes = new ArrayList<>();
        while (rs.next()) {
            commentVotes.add(this.convertResultSetToDomainModel(rs));
        }
        return commentVotes;
    }

    @Override
    protected String getUpdateStatement(String varName, String newValue, String whereField, String whereValue) {
        return null;
    }
}
