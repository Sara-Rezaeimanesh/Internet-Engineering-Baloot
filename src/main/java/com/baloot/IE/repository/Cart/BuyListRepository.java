package com.baloot.IE.repository.Cart;

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

public class BuyListRepository extends Repository<CartItem, String> {
    private static BuyListRepository instance;

    private static final String COLUMNS = " cartId, productId, quantity";
    private static final String TABLE_NAME = "BUYLIST";
    private final ProductRepository productRepository = ProductRepository.getInstance();

    public static BuyListRepository getInstance() {
        if (instance == null) {
            try {
                instance = new BuyListRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in BuyListRepository.create query.");
            }
        }
        return instance;
    }

    private BuyListRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(cartId CHAR(50),\nproductId CHAR(225),\n quantity CHAR(225),\nPRIMARY KEY(cartId, productId),"
                        +  "\nforeign key (cartId) references CART(cartId),\nforeign key (productId) references PRODUCTS(id));",
                        TABLE_NAME)
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement(String field_name) {
        // discountCode
        return String.format("SELECT * FROM %s c WHERE c.%s = ?;", TABLE_NAME, field_name);
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, String username) throws SQLException {
        st.setString(1, username);
    }


    @Override
    protected String getInsertStatement() {
        return String.format("INSERT INTO %s (cartId, productId, quantity)\n" +
                                "VALUES (?, ?, ?)\n" +
                            "ON DUPLICATE KEY UPDATE quantity = ?;", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, CartItem data) throws SQLException {
        st.setString(1, String.valueOf(data.getCartId()));
        st.setString(2, String.valueOf(data.getProduct().getId()));
        st.setString(3, String.valueOf(data.getQuantity()));
        st.setString(4, String.valueOf(data.getQuantity()+1));
    }

    @Override
    protected String getFindAllStatement(String searchString) {
        return String.format("SELECT * FROM %s where "+ searchString + ";", TABLE_NAME);
    }

    @Override
    protected CartItem convertResultSetToDomainModel(ResultSet rs) {
        try{
            return new CartItem(Integer.parseInt(rs.getString(1)), productRepository.findByField(rs.getString(2), "id"), Integer.parseInt(rs.getString(3)));
        }
        catch (Exception e){
            return null;
        }
    }

    @Override
    protected ArrayList<CartItem> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<CartItem> CartItems = new ArrayList<>();
        while (rs.next()) {
            System.out.println("x-x");
            CartItems.add(this.convertResultSetToDomainModel(rs));
        }
        return CartItems;
    }

    @Override
    protected String getUpdateStatement(String varName, String newValue, String whereField, String whereValue) {
        return String.format("update %s set %s = %s where %s = %s;",
                TABLE_NAME, varName, newValue, whereField, whereValue);
    }

    public void delete(String cartId, String productId) {
        String statement =  String.format("delete from %s b where b.%s = %s and b.%s = %s ", TABLE_NAME, "cartId", cartId, "productId", productId);
        try {
            Connection con = ConnectionPool.getConnection();
            PreparedStatement st = con.prepareStatement(statement);
            System.out.println(st);
            try {
                st.executeUpdate();
                st.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println("error in delete query.");
                throw ex;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}