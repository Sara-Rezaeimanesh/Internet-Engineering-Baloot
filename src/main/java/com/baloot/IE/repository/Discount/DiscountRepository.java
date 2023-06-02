package com.baloot.IE.repository.Discount;

import com.baloot.IE.domain.Discount.Discount;
import com.baloot.IE.repository.ConnectionPool;
import com.baloot.IE.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DiscountRepository extends Repository<Discount, String> {
    private static DiscountRepository instance;

    public static DiscountRepository getInstance() {
        if (instance == null) {
            try {
                instance = new DiscountRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in DiscountRepository.create query.");
            }
        }
        return instance;
    }

    private DiscountRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                "CREATE TABLE IF NOT EXISTS DISCOUNTS " +
                        "(discountCode CHAR(50),\ndiscount CHAR(225),\nPRIMARY KEY(discountCode));");
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement(String field_name) {
        return "SELECT * FROM DISCOUNTS d WHERE d.discountCode = ?;";
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, String username) throws SQLException {
        st.setString(1, username);
    }

    @Override
    protected String getInsertStatement() {
        return "INSERT IGNORE INTO USERS (discountCode, discount) VALUES(?,?)";
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Discount data) throws SQLException {
        st.setString(1, data.getDiscountCode());
        st.setString(2, String.valueOf(data.getDiscount()) );
    }

    @Override
    protected String getFindAllStatement(String searchString) {
        return "SELECT * FROM USERS;";
    }

    @Override
    protected Discount convertResultSetToDomainModel(ResultSet rs) {
        try{
            return new Discount(rs.getString(1), rs.getString(2));
        }
        catch (Exception e){
            return null;
        }
    }

    @Override
    protected ArrayList<Discount> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<Discount> discounts = new ArrayList<>();
        while (rs.next()) {
            discounts.add(this.convertResultSetToDomainModel(rs));
        }
        return discounts;
    }

    @Override
    protected String getUpdateStatement(String varName, String newValue, String whereField, String whereValue) {
        return null;
    }
}