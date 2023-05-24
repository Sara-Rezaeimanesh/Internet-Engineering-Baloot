package com.baloot.IE.repository.Cart;

import com.baloot.IE.domain.Cart.Cart;
import com.baloot.IE.domain.Cart.CartItem;
import com.baloot.IE.domain.Product.Product;
import com.baloot.IE.repository.ConnectionPool;
import com.baloot.IE.repository.Product.ProductRepository;
import com.baloot.IE.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CartRepository extends Repository<Cart, String> {
    private static CartRepository instance;

    private static final String COLUMNS = " cartId, discount, total, no_items";
    private static final String TABLE_NAME = "CART";
    private final ProductRepository productRepository = ProductRepository.getInstance();

    public static CartRepository getInstance() {
        if (instance == null) {
            try {
                instance = new CartRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in CartItemRepository.create query.");
            }
        }
        return instance;
    }

    private CartRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(username CHAR(50),cartId CHAR(50),\ndiscount CHAR(225),\n total CHAR(225),\n no_items CHAR(225)\nPRIMARY KEY(cartId)"
                                +  "\nforeign key (username) references USER(username));",
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
    protected void fillFindByIdValues(PreparedStatement st, String username) throws SQLException {
        st.setString(1, username);
    }


    @Override
    protected String getInsertStatement() {
        return String.format("INSERT IGNORE INTO %s(cartId, discount, total, no_items) VALUES(?,?,?)", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Cart data) throws SQLException {
        st.setString(1, String.valueOf(data.getCartId()));
        st.setString(2, String.valueOf(data.getDiscount()));
        st.setString(3, String.valueOf(data.getTotal()));
        st.setString(4, String.valueOf(data.getNo_items()));
    }

    @Override
    protected String getFindAllStatement() {
        return String.format("SELECT * FROM %s;", TABLE_NAME);
    }

    @Override
    protected Cart convertResultSetToDomainModel(ResultSet rs) {
//        try{
//            return new Cart(rs.getString(1));
//        }
//        catch (Exception e){
//            return new Cart();
//        }
        return null;
    }

    @Override
    protected ArrayList<Cart> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
//        ArrayList<CartItem> CartItems = new ArrayList<>();
//        while (rs.next()) {
//            CartItems.add(this.convertResultSetToDomainModel(rs));
//        }
//        return CartItems;
        return null;
    }
}