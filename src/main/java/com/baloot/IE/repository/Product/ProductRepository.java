package com.baloot.IE.repository.Product;

import com.baloot.IE.domain.Product.Product;
import com.baloot.IE.domain.User.User;
import com.baloot.IE.repository.ConnectionPool;
import com.baloot.IE.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductRepository extends Repository<Product, String> {
    private static ProductRepository instance;

    private static final String COLUMNS = " id, name, providerId, price, inStock, rating, image";
    private static final String TABLE_NAME = "PRODUCTS";

    public static ProductRepository getInstance() {
        if (instance == null) {
            try {
                instance = new ProductRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in ProductRepository.create query.");
            }
        }
        return instance;
    }

    private ProductRepository() throws SQLException {//id, name, providerId, price, inStock, rating, image
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format(
                        "\"CREATE TABLE IF NOT EXISTS %s (\\n\" +\n" +
                                "\"    id varchar(255),\\n\" +\n" +
                                "\"    name varchar(255),\\n\" +\n" +
                                "\"    providerId varchar(255) not null,\\n\" +\n" +
                                "\"    price varchar(255) not null,\\n\" +\n" +
                                "\"    inStock varchar(255),\\n\" +\n" +
                                "\"    rating varchar(255),\\n\" +\n" +
                                "\"    image text,\\n\" +\n" +
                                "\"    primary key(id),\\n\" +\n" +
                                "\"    foreign key (providerId) references PROVIDER(pid)\\n\" +\n" +
                                "\");\",",
                        TABLE_NAME)
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement() {
        return String.format("SELECT * FROM %s p WHERE p.id = ?;", TABLE_NAME);
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, String username) throws SQLException {
        st.setString(1, username);
    }

    @Override
    protected String getInsertStatement() {
        return String.format("INSERT IGNORE INTO %s(id, name, providerId, price, inStock, rating, image) VALUES(?,?,?,?,?,?)", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Product data) throws SQLException {
        st.setString(1, String.valueOf(data.getId()));
        st.setString(2, data.getName());
        st.setString(3, String.valueOf(data.getProviderId()));
        st.setString(4, String.valueOf(data.getPrice()));
        st.setString(5, String.valueOf(data.getInStock()));
        st.setString(6, data.getImage());
    }

    @Override
    protected String getFindAllStatement() {
        return String.format("SELECT * FROM %s;", TABLE_NAME);
    }

    @Override
    protected Product convertResultSetToDomainModel(ResultSet rs) {
        try{
            return new Product(Integer.parseInt(rs.getString(1)), rs.getString(2), Integer.parseInt(rs.getString(3)),
                    Integer.parseInt(rs.getString(4)),new ArrayList<>(), Float.parseFloat(rs.getString(5)), Integer.parseInt(rs.getString(6)), rs.getString(7));
        }
        catch (Exception e){
            return new Product();
        }
    }

    @Override
    protected ArrayList<Product> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<Product> products = new ArrayList<>();
        while (rs.next()) {
            products.add(this.convertResultSetToDomainModel(rs));
        }
        return products;
    }
}
