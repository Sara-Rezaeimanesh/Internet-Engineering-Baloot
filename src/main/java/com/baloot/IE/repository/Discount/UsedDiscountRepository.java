package com.baloot.IE.repository.Discount;

import com.baloot.IE.domain.Discount.UsedDiscount;
import com.baloot.IE.repository.ConnectionPool;
import com.baloot.IE.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsedDiscountRepository extends Repository<UsedDiscount, ArrayList<String>> {
    private static UsedDiscountRepository instance;

    private static final String COLUMNS = " discountCode, username";
    private static final String TABLE_NAME = "USED_DISCOUNTS";

    public static UsedDiscountRepository getInstance() {
        if (instance == null) {
            try {
                instance = new UsedDiscountRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in UsedDiscountRepository.create query.");
            }
        }
        return instance;
    }

    private UsedDiscountRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(discountCode CHAR(50),\nusername CHAR(225),\nPRIMARY KEY(discountCode, username),"
                                +  "\nforeign key (discountCode) references DISCOUNTS(discountCode),\n foreign key (username) references USERS(username));",
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
    protected String getSearchStatement(String field_name) {
        return String.format("SELECT * FROM %s where username =  ? and discountCode = ?;", TABLE_NAME);
    }

    @Override
    protected void fillSearchValues(PreparedStatement st, ArrayList<String> fields) throws SQLException {
        st.setString(1, fields.get(0));
        st.setString(2, fields.get(1));
    }

    @Override
    protected void fillUpdateValues(PreparedStatement st, String field, ArrayList<String> where) throws SQLException {

    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, ArrayList<String> username) throws SQLException {
        st.setString(1, username.get(0));
    }


    @Override
    protected String getInsertStatement() {
        return String.format("INSERT IGNORE INTO %s(discountCode, username) VALUES(?,?)", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, UsedDiscount data) throws SQLException {
        st.setString(1, data.getDiscountCode());
        st.setString(2, String.valueOf(data.getUsername()));
    }

    @Override
    protected String getFindAllStatement() {
        return String.format("SELECT * FROM %s;", TABLE_NAME);
    }

    @Override
    protected UsedDiscount convertResultSetToDomainModel(ResultSet rs) {
        try{
            return new UsedDiscount(rs.getString(1), rs.getString(2));
        }
        catch (Exception e){
            return null;
        }
    }

    @Override
    protected ArrayList<UsedDiscount> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<UsedDiscount> discounts = new ArrayList<>();
        while (rs.next()) {
            discounts.add(this.convertResultSetToDomainModel(rs));
        }
        return discounts;
    }

    @Override
    protected String getUpdateStatement(String varName, String newValue, String whereField, ArrayList<String> whereValue) {
        return null;
    }
}