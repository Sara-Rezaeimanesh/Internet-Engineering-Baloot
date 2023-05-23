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

public class PurchaseListRepository extends Repository<CartItem, String> {
    private static PurchaseListRepository instance;

    private static final String COLUMNS = " cartId, productId, quantity";
    private static final String TABLE_NAME = "PURCHASELIST";
    private final ProductRepository productRepository = ProductRepository.getInstance();

    public static PurchaseListRepository getInstance() {
        if (instance == null) {
            try {
                instance = new PurchaseListRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in CartItemRepository.create query.");
            }
        }
        return instance;
    }

    private PurchaseListRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(cartId CHAR(50),\nproductId CHAR(225),\n quantity CHAR(225),\nPRIMARY KEY(username, productId)"
                                +  "\nforeign key (cartId) references CART(cartId));",
                        TABLE_NAME)
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement() {
        return String.format("SELECT * FROM %s c WHERE c.discountCode = ?;", TABLE_NAME);
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, String username) throws SQLException {
        st.setString(1, username);
    }


    @Override
    protected String getInsertStatement() {
        return String.format("INSERT IGNORE INTO %s(cartId, productId, quantity) VALUES(?,?,?)", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, CartItem data) throws SQLException {
        st.setString(1, String.valueOf(data.getCartId()));
        st.setString(2, String.valueOf(data.getProduct().getId()));
        st.setString(2, String.valueOf(data.getQuantity()));
    }

    @Override
    protected String getFindAllStatement() {
        return String.format("SELECT * FROM %s;", TABLE_NAME);
    }

    @Override
    protected CartItem convertResultSetToDomainModel(ResultSet rs) {
        try{
            return new CartItem(productRepository.findById(rs.getString(1)), Integer.parseInt(rs.getString(2)), Integer.parseInt(rs.getString(2)));
        }
        catch (Exception e){
            return new CartItem(new Product(),-1,-1);
        }
    }

    @Override
    protected ArrayList<CartItem> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<CartItem> CartItems = new ArrayList<>();
        while (rs.next()) {
            CartItems.add(this.convertResultSetToDomainModel(rs));
        }
        return CartItems;
    }
}