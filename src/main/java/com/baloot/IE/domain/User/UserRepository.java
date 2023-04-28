package com.baloot.IE.domain.User;

import com.baloot.IE.domain.Amazon.Initializer;

import java.util.ArrayList;

public class UserRepository {
    ArrayList<User> users;
    private static UserRepository instance;
    private UserRepository() throws Exception {
        Initializer initializer = new Initializer();
        users = initializer.getUsersFromAPI("users");
        users.forEach(User::initialize);
    }
    public static UserRepository getInstance() throws Exception {
        if(instance == null)
            instance = new UserRepository();
        return instance;
    }
    public User findUserById(String uid) {
        for(User u : users)
            if(u.userNameEquals(uid))
                return u;

        return null;
    }
    public boolean DoesUserExist(String username, String password){
        User user = findUserById(username);
        return user != null && user.isPassEqual(password);
    }

    public ArrayList<User> getAllUsers() {
        return users;
    }

    public void addUser(User user) {
        for (User user_ : users)
            if (user_.userNameEquals(user.getUsername())) {
                throw new IllegalArgumentException("Username already exists. Please Login.");
            }
        users.add(user);
    }
}
