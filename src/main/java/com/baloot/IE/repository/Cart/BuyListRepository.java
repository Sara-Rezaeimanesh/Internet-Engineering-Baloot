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
import java.util.Objects;

public class BuyListRepository extends Repository<CartItem, ArrayList<String>> {
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
                "CREATE TABLE IF NOT EXISTS BUYLIST " +
                        "(cartId MEDIUMINT,\nproductId CHAR(225),\n quantity INT,\nPRIMARY KEY(cartId, productId),"
                +  "\nforeign key (cartId) references CART(cartId),\nforeign key (productId) references PRODUCTS(id));");
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement(String field_name) {
        // discountCode
        return "SELECT * FROM BUYLIST c WHERE c.productId = ? and c.cartId = ?;";
    }

    @Override
    protected String getSearchStatement(String field_name) {
        if(Objects.equals(field_name, "cartId"))
            return "SELECT * FROM BUYLIST where cartId = ?;";
        else
            throw new IllegalArgumentException("YOU CAN'T SQL INJECT ME YOU MORON!");
    }

    @Override
    protected void fillSearchValues(PreparedStatement st, ArrayList<String> fields) throws SQLException {

    }

    @Override
    protected void fillUpdateValues(PreparedStatement st, ArrayList<String> fields) throws SQLException {
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, ArrayList<String> fields) throws SQLException {
        st.setString(1, fields.get(0));
        st.setString(2, fields.get(1));
    }


    @Override
    protected String getInsertStatement() {
        return "INSERT INTO BUYLIST (cartId, productId, quantity)\n" +
                                "VALUES (?, ?, ?)\n";
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, CartItem data) throws SQLException {
        st.setString(1, String.valueOf(data.getCartId()));
        st.setString(2, String.valueOf(data.getProduct().getId()));
        st.setString(3, String.valueOf(data.getQuantity()));
    }

    @Override
    protected String getFindAllStatement() {
        return "SELECT * FROM BUYLIST;";
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
            CartItems.add(this.convertResultSetToDomainModel(rs));
        }
        return CartItems;
    }

    @Override
    protected String getUpdateStatement(String varName, String newValue, String productId, String cartId) {
        return String.format("update %s set %s = %s where productId = %s and cartId = %s;",
                TABLE_NAME, varName, newValue, productId, cartId);
    }

    public void delete(String cartId, String productId) {
        String statement =  String.format("delete from %s b where b.%s = %s and b.%s = %s ", TABLE_NAME, "cartId", cartId, "productId", productId);
        try {
            Connection con = ConnectionPool.getConnection();
            PreparedStatement st = con.prepareStatement(statement);
//            System.out.println(st);
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