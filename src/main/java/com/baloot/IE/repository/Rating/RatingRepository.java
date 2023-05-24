package com.baloot.IE.repository.Rating;

import com.baloot.IE.domain.Rating.Rating;
import com.baloot.IE.repository.ConnectionPool;
import com.baloot.IE.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RatingRepository extends Repository<Rating, String> {
    private static RatingRepository instance;

    private static final String COLUMNS = " username, productId, score";
    private static final String TABLE_NAME = "RATINGS";

    public static RatingRepository getInstance() {
        if (instance == null) {
            try {
                instance = new RatingRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in RatingRepository.create query.");
            }
        }
        return instance;
    }

    private RatingRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(username CHAR(50),\nproductId CHAR(225), score CHAR(225)\n,PRIMARY KEY(username, productId),\n" +
                                "foreign key (username) references USERS(username),\n foreign key (productId) references PRODUCTS(id));",
                        TABLE_NAME)
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement(String field_name) {
        return String.format("SELECT * FROM %s d WHERE d.%s = ?;", TABLE_NAME, field_name);
    }


    @Override
    protected void fillFindByIdValues(PreparedStatement st, String username) throws SQLException {
        st.setString(1, username);
    }


    @Override
    protected String getInsertStatement() {
        return String.format("IF EXISTS (SELECT 1 FROM %s WHERE username = ? and productId = ?)" +
                                "UPDATE yourTable SET score = ? WHERE username = ? and productId = ?;" +
                            "ELSE" +
                                "INSERT INTO %s (username, productId, score) VALUES (?, ?, ?)", TABLE_NAME, TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Rating data) throws SQLException {
        st.setString(1, String.valueOf(data.getUsername()));
        st.setString(2, String.valueOf(data.getProductId()));
        st.setString(3, String.valueOf(data.getScore()));
        st.setString(4, String.valueOf(data.getUsername()));
        st.setString(5, String.valueOf(data.getProductId()));

        st.setString(6, String.valueOf(data.getUsername()));
        st.setString(7, String.valueOf(data.getProductId()));
        st.setString(8, String.valueOf(data.getScore()));
    }

    @Override
    protected String getFindAllStatement(String searchString) {
        return String.format("SELECT * FROM %s;", TABLE_NAME);
    }

    @Override
    protected Rating convertResultSetToDomainModel(ResultSet rs) {
        try{
            return new Rating(rs.getString(1), Integer.parseInt(rs.getString(2)), Float.parseFloat(rs.getString(3)));
        }
        catch (Exception e){
            return new Rating();
        }
    }

    @Override
    protected ArrayList<Rating> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<Rating> ratings = new ArrayList<>();
        while (rs.next()) {
            ratings.add(this.convertResultSetToDomainModel(rs));
        }
        return ratings;
    }

    @Override
    protected String getUpdateStatement(String varName, String newValue, String whereField, String whereValue) {
        return null;
    }

    public float calculateRating(int id) {
        String statement =  String.format("select avg(rating) from %s r where r.id = %s", TABLE_NAME, id);
        try {
            Connection con = ConnectionPool.getConnection();
            PreparedStatement st = con.prepareStatement(statement);
            try {
                return st.executeUpdate();
            } catch (SQLException ex) {
                System.out.println("error in Mapper.delete query.");
                throw ex;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}