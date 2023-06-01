package com.baloot.IE.repository;

import com.baloot.IE.domain.Product.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Repository<T, I> {
    abstract protected String getFindByIdStatement(String field_name);

    abstract protected void fillFindByIdValues(PreparedStatement st, I id) throws SQLException;

    abstract protected String getInsertStatement();

    abstract protected void fillInsertValues(PreparedStatement st, T data) throws SQLException;

    abstract protected String getFindAllStatement(String searchString);

    abstract protected T convertResultSetToDomainModel(ResultSet rs) throws SQLException;

    abstract protected ArrayList<T> convertResultSetToDomainModelList(ResultSet rs) throws SQLException;

    abstract protected String getUpdateStatement(String varName, String newValue, String whereField, String whereValue);

    public T findByField(I id, String field_name) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindByIdStatement(field_name));
        fillFindByIdValues(st, id);
//        System.out.println(st);
        try {
            ResultSet resultSet = st.executeQuery();
            if (!resultSet.next()) {
                st.close();
                con.close();
                return null;
            }
            T result = convertResultSetToDomainModel(resultSet);
            st.close();
            con.close();
            return result;
        } catch (Exception e) {
            st.close();
            con.close();
            System.out.println("error in Repository.find query.");
            e.printStackTrace();
            throw e;
        }
    }

    public void insert(T obj) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getInsertStatement());
        fillInsertValues(st, obj);
//        System.out.println(st);
        try {
            st.execute();
            st.close();
            con.close();
        } catch (Exception e) {
            st.close();
            con.close();
            System.out.println("error in Repository.insert query.");
            e.printStackTrace();
        }
    }

    public String getFindByNameStatement() {
        return null;
    }

    public ArrayList<T> findAll(String searchString) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindAllStatement(searchString));
//        System.out.println(st);
        try {
            ResultSet resultSet = st.executeQuery();
            if (resultSet == null) {
                st.close();
                con.close();
                return new ArrayList<>();
            }
            ArrayList<T> result = convertResultSetToDomainModelList(resultSet);
            st.close();
            con.close();
            return result;
        } catch (Exception e) {
            st.close();
            con.close();
            System.out.println("error in Repository.findAll query.");
            e.printStackTrace();
            throw e;
        }
    }

    public void update(String varName, String newValue, String whereField, String whereValue) {
        String statement = getUpdateStatement(varName, newValue, whereField, whereValue);
//        System.out.println(statement);
        try {
            Connection con = ConnectionPool.getConnection();
            PreparedStatement st = con.prepareStatement(statement);

            try {
                con.setAutoCommit(false);
                st.executeUpdate();
                con.commit();
                st.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println("error in update query.");
                throw ex;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<T> executeQuery(String query) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(query);
//        System.out.println(st);
        try {
            ResultSet resultSet = st.executeQuery();
            if (resultSet == null) {
                st.close();
                con.close();
                return new ArrayList<>();
            }
            ArrayList<T> result = convertResultSetToDomainModelList(resultSet);
            st.close();
            con.close();
            return result;
        } catch (Exception e) {
            st.close();
            con.close();
            System.out.println("error in Repository.findAll query.");
            e.printStackTrace();
            throw e;
        }
    }
}