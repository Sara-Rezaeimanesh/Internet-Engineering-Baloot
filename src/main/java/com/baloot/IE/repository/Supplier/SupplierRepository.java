package com.baloot.IE.repository.Supplier;

import com.baloot.IE.domain.Supplier.Supplier;
import com.baloot.IE.repository.ConnectionPool;
import com.baloot.IE.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierRepository extends Repository<Supplier, String> {
    private static SupplierRepository instance;

    private static final String COLUMNS = " id, name, registeryDate";
    private static final String TABLE_NAME = "PROVIDERS";

    public static SupplierRepository getInstance() {
        if (instance == null) {
            try {
                instance = new SupplierRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in UserManager.create query.");
            }
        }
        return instance;
    }

    private SupplierRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(username CHAR(50),\nname CHAR(225),\nregisteryDate CHAR(225),",
                        TABLE_NAME)
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement() {
        return String.format("SELECT * FROM %s s WHERE s.username = ?;", TABLE_NAME);
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, String username) throws SQLException {
        st.setString(1, username);
    }

    @Override
    protected String getInsertStatement() {
        return String.format("INSERT IGNORE INTO %s(id, name, registeryDate) VALUES(?,?,?)", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Supplier data) throws SQLException {
        st.setString(1, String.valueOf(data.getId()));
        st.setString(2, data.getName());
        st.setString(3, data.getRegisteryDate());
    }

    @Override
    protected String getFindAllStatement() {
        return String.format("SELECT * FROM %s;", TABLE_NAME);
    }

    @Override
    protected Supplier convertResultSetToDomainModel(ResultSet rs) {
        try{
            return new Supplier(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3));
        }
        catch (Exception e){
            return new Supplier(-1, "-1","-1");
        }
    }

    @Override
    protected ArrayList<Supplier> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<Supplier> suppliers = new ArrayList<>();
        while (rs.next()) {
            suppliers.add(this.convertResultSetToDomainModel(rs));
        }
        return suppliers;
    }
}