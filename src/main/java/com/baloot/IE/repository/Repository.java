package com.baloot.IE.repository;

import com.baloot.IE.domain.Product.Product;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Repository<T, I> {
    abstract protected String getFindByIdStatement(String field_name);
    abstract protected String getSearchStatement(String field_name);
    abstract protected void fillSearchValues(PreparedStatement st, I fields) throws SQLException;
    abstract protected void fillUpdateValues(PreparedStatement st, String field, I where) throws SQLException;
    abstract protected void fillFindByIdValues(PreparedStatement st, I fields) throws SQLException;

    abstract protected String getInsertStatement();

    abstract protected void fillInsertValues(PreparedStatement st, T data) throws SQLException;

    abstract protected String getFindAllStatement();

    abstract protected T convertResultSetToDomainModel(ResultSet rs) throws SQLException;

    abstract protected ArrayList<T> convertResultSetToDomainModelList(ResultSet rs) throws SQLException;

    abstract protected String getUpdateStatement(String varName, String newValue, String whereField, I whereValue);


    public T findByField(I id, String field_name) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindByIdStatement(field_name));
        fillFindByIdValues(st, id);
        try {
            ResultSet resultSet = st.executeQuery();
            if (!resultSet.next()) {
                st.close();
                con.close();
                return null;
            }
            T result = convertResultSetToDomainModel(resultSet);
            resultSet.close();
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
        System.out.println(st);
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

    public ArrayList<T> findAll() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindAllStatement());
        try {
            ResultSet resultSet = st.executeQuery();
            if (resultSet == null) {
                st.close();
                con.close();
                return new ArrayList<>();
            }
            ArrayList<T> result = convertResultSetToDomainModelList(resultSet);
            resultSet.close();
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

    public ArrayList<T> search(I id, String field_name) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getSearchStatement(field_name));
        fillSearchValues(st, id);
        try {
            ResultSet resultSet = st.executeQuery();
            if (resultSet == null) {
                st.close();
                con.close();
                return new ArrayList<>();
            }
            ArrayList<T> result = convertResultSetToDomainModelList(resultSet);
            resultSet.close();
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

    public void update(String varName, String newValue, String whereField, I whereValue) {
        String statement = getUpdateStatement(varName, newValue, whereField, whereValue);
        try {
            Connection con = ConnectionPool.getConnection();
            PreparedStatement st = con.prepareStatement(statement);
            fillUpdateValues(st, newValue, whereValue);
            System.out.println(st);
            try {
                con.setAutoCommit(false);
                st.executeUpdate();
                con.commit();
                st.close();
                con.close();
            } catch (SQLException ex) {
                st.close();
                con.close();
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
        System.out.println(st);
        try{
            ResultSet resultSet = st.executeQuery();
            if (resultSet == null) {
                st.close();
                con.close();
                return new ArrayList<>();
            }
            ArrayList<T> result = convertResultSetToDomainModelList(resultSet);
            resultSet.close();
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