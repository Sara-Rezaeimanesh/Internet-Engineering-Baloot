package com.baloot.IE.repository.Cart;

import com.baloot.IE.domain.Cart.Cart;
import com.baloot.IE.domain.Cart.CartItem;
import com.baloot.IE.repository.ConnectionPool;
import com.baloot.IE.repository.Repository;
import com.baloot.IE.utitlity.StringUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CartRepository extends Repository<Cart, String> {
    private static CartRepository instance;

    private static final String COLUMNS = " username,cartId, discount, total, no_items";
    private static final String TABLE_NAME = "CART";
    public static CartRepository getInstance() {
        if (instance == null) {
            try {
                instance = new CartRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in CartRepository.create query.");
            }
        }
        return instance;
    }

    private CartRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(username CHAR(50),\n cartId MEDIUMINT NOT NULL AUTO_INCREMENT,\ndiscount INTEGER,\n total INTEGER,\n no_items INTEGER,\nPRIMARY KEY(cartId),"
                                +  "\nforeign key (username) references USERS(username));",
                        TABLE_NAME)
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement(String field_name) {
            return String.format("SELECT * FROM %s c WHERE c.%s = ?;", TABLE_NAME,field_name);
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, String username, String field_name) throws SQLException {
        st.setString(1, username);
    }

    @Override
    protected String getInsertStatement() {
        return String.format("INSERT IGNORE INTO %s(username, discount, total, no_items) VALUES(?,?,?,?)", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Cart data) throws SQLException {

        st.setString(1, String.valueOf(data.getUsername()));
        st.setString(2, String.valueOf(data.getDiscount()));
        st.setString(3, String.valueOf(data.getTotal()));
        st.setString(4, String.valueOf(data.getNo_items()));
    }

    @Override
    protected String getFindAllStatement(String searchString) {
        return String.format("SELECT * FROM %s where "+ searchString + ";", TABLE_NAME);
    }

    @Override
    protected Cart convertResultSetToDomainModel(ResultSet rs) {
        try{
            int discount = Integer.parseInt(rs.getString(3));
            int total = Integer.parseInt(rs.getString(4));
            int no_items = Integer.parseInt(rs.getString(5));
            return new Cart(rs.getString(1), Integer.parseInt(rs.getString(2)),discount, total, no_items);
        }
        catch (Exception e){
            return null;
        }
    }

    @Override
    protected ArrayList<Cart> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<Cart> Carts = new ArrayList<>();
        while (rs.next()) {
            Carts.add(this.convertResultSetToDomainModel(rs));
        }
        return Carts;
    }

    @Override
    protected String getUpdateStatement(String varName, String newValue, String whereField, String whereValue) {
        return String.format("update %s set %s = %s where %s = %s;",
                TABLE_NAME, varName, newValue, whereField, whereValue);
    }

    public void delete(String username) {
        String statement =  String.format("delete from %s b where b.%s = %s", TABLE_NAME, "username", StringUtility.quoteWrapper(username));
        try {
            Connection con = ConnectionPool.getConnection();
            PreparedStatement st = con.prepareStatement(statement);
//            System.out.println(st);
            try {
                st.executeUpdate();
                st.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println("error in cart.delete query.");
                throw ex;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}