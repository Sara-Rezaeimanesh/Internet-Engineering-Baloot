package com.baloot.IE.repository.User;

import com.baloot.IE.domain.User.User;
import com.baloot.IE.repository.ConnectionPool;
import com.baloot.IE.repository.Repository;
import com.baloot.IE.utitlity.StringUtility;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserRepository extends Repository<User, String> {
    private static UserRepository instance;

    public static UserRepository getInstance() {
        if (instance == null) {
            try {
                instance = new UserRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in UserManager.create query.");
            }
        }
        return instance;
    }

    private UserRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                "CREATE TABLE IF NOT EXISTS USERS " +
                 "(username CHAR(50),\npassword CHAR(225),\nemail CHAR(225),"  +
                 "\nbirthDate DATE,\n address CHAR(200), credit FLOAT,\nPRIMARY KEY(username, email),\n" +
                        "unique (email));");
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement(String field_name) {
        return String.format("SELECT * FROM USERS u WHERE u.%s = ?;", field_name);
    }

    @Override
    protected String getSearchStatement(String field_name) {
        return null;
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
        return "INSERT IGNORE INTO USERS (username, password, email, birthDate, address, credit) VALUES(?,?,?,?,?,?)";
    }



    @Override
    protected void fillInsertValues(PreparedStatement st, User data) throws SQLException {
        st.setString(1, data.getUsername());
        st.setString(2, StringUtility.hashPassword(data.getPassword()));
        st.setString(3, data.getEmail());
        st.setString(4, data.getBirthDate());
        st.setString(5, data.getAddress());
        st.setString(6, String.valueOf(data.getCredit()));
    }

    @Override
    protected String getFindAllStatement() {
        return "SELECT * FROM USERS;";
    }

    @Override
    protected User convertResultSetToDomainModel(ResultSet rs) {
        try{
            return new User(rs.getString(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5),(int) Double.parseDouble((rs.getString(6))));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    protected ArrayList<User> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        while (rs.next()) {
            users.add(this.convertResultSetToDomainModel(rs));
        }
        return users;
    }

    @Override
    protected String getUpdateStatement(String varName, String newValue, String whereField, String whereValue) {
        return String.format("update %s set %s = %s where %s = %s;",
                "USERS", varName, newValue, whereField, whereValue);
    }
}