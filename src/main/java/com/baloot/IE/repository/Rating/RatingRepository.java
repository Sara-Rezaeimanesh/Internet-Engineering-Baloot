package com.baloot.IE.repository.Rating;

import com.baloot.IE.domain.Rating.Rating;
import com.baloot.IE.repository.ConnectionPool;
import com.baloot.IE.repository.Repository;

import java.sql.*;
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
        return String.format("INSERT INTO %s (username, productId, score)\n" +
                            "VALUES (?, ?, ?)\n" +
                            "ON DUPLICATE KEY UPDATE score = ?;", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Rating data) throws SQLException {
        st.setString(1, String.valueOf(data.getUsername()));
        st.setString(2, String.valueOf(data.getProductId()));
        st.setString(3, String.valueOf(data.getScore()));

        st.setString(4, String.valueOf(data.getScore()));
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
            return null;
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
        String query = String.format("SELECT avg(r.score) FROM %s r WHERE r.productId = %s", TABLE_NAME, id);
        if (query != null && !query.isEmpty()) {
            try {
                Connection con = ConnectionPool.getConnection();
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                // Move the cursor to the first row
                if (resultSet.next()) {
                    return resultSet.getFloat(1);
                }

                resultSet.close();
                statement.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid SQL query");
        }
        return 0;
    }
}