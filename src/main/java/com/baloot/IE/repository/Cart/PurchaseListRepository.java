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

    private static final String COLUMNS = " username, productId, quantity";
    private static final String TABLE_NAME = "PURCHASELIST";
    private final ProductRepository productRepository = ProductRepository.getInstance();
    private final CartRepository cartRepository = CartRepository.getInstance();

    public static PurchaseListRepository getInstance() {
        if (instance == null) {
            try {
                instance = new PurchaseListRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in PurchaseListRepository.create query.");
            }
        }
        return instance;
    }

    private PurchaseListRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(id MEDIUMINT NOT NULL AUTO_INCREMENT,username CHAR(50),\nproductId CHAR(225),\n quantity CHAR(225),\nPRIMARY KEY(id),"
                                +  "\nforeign key (username) references USERS(username),\nforeign key (productId) references PRODUCTS(id));",
                        TABLE_NAME)
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement(String field_name) {
        return String.format("SELECT * FROM %s c WHERE c.%s = ?;", TABLE_NAME, field_name);
    }

    @Override
    protected String getSearchStatement(String field_name) {
        return "";
    }

    @Override
    protected void fillSearchValues(PreparedStatement st, String fields) throws SQLException {

    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, String username) throws SQLException {
        st.setString(1, username);
    }


    @Override
    protected String getInsertStatement() {
        return String.format("INSERT IGNORE INTO %s(username, productId, quantity) VALUES(?,?,?)", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, CartItem data) throws SQLException {
        String username = cartRepository.findByField(String.valueOf(data.getCartId()),"cartId").getUsername();
        st.setString(1, username);
        st.setString(2, String.valueOf(data.getProduct().getId()));
        st.setString(3, String.valueOf(data.getQuantity()));
    }

    @Override
    protected String getFindAllStatement() {
        return String.format("SELECT * FROM %s;", TABLE_NAME);
    }

    @Override
    protected CartItem convertResultSetToDomainModel(ResultSet rs) {
        try{
            return new CartItem(Integer.parseInt(rs.getString(1)),productRepository.findByField(rs.getString(3), "id"),  Integer.parseInt(rs.getString(4)));
        }
        catch (Exception e){
            return null;
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
    @Override
    protected String getUpdateStatement(String varName, String newValue, String whereField, String whereValue) {
        return "";
    }

    @Override
    protected void fillUpdateValues(PreparedStatement st, String field, String where) throws SQLException {

    }
}