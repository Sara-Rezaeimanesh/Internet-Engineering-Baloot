package com.baloot.IE.domain.User;

import com.baloot.IE.repository.ConnectionPool;
import com.baloot.IE.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserRepository extends Repository<User, String> {
    private static UserRepository instance;

    private static final String COLUMNS = " username, password, email, birthDate, address, credit";
    private static final String TABLE_NAME = "USERS";

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
        System.out.println(String.format(
                "CREATE TABLE IF NOT EXISTS %s " +
                        "(username CHAR(50),\npassword CHAR(225),\nemail CHAR(225),"  +
                        "\nbirthDate DATE,\n address CHAR(200), credit INTEGER\nPRIMARY KEY(username));",
                TABLE_NAME));
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                         "(username CHAR(50),\npassword CHAR(225),\nemail CHAR(225),"  +
                          "\nbirthDate DATE,\n address CHAR(200), credit INTEGER,\nPRIMARY KEY(username));",
                        TABLE_NAME)
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement() {
        return String.format("SELECT * FROM %s u WHERE u.id = ?;", TABLE_NAME);
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, String username) throws SQLException {
        st.setString(1, username);
    }

    @Override
    protected String getInsertStatement() {
        return String.format("INSERT INTO %s(username, password, email, birthDate, address, credit) VALUES(?,?,?,?,?,?)", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, User data) throws SQLException {
        st.setString(1, data.getUsername());
        st.setString(2, data.getPassword());
        st.setString(3, data.getEmail());
        st.setString(4, data.getBirthDate());
        st.setString(5, data.getAddress());
        st.setString(6, String.valueOf(data.getCredit()));
    }

    @Override
    protected String getFindAllStatement() {
        return String.format("SELECT * FROM %s;", TABLE_NAME);
    }

    @Override
    protected User convertResultSetToDomainModel(ResultSet rs) {
        try{
            return new User(rs.getString(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), Integer.parseInt(rs.getString(6)));
        }
        catch (Exception e){
            return new User();
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
}