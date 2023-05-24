package com.baloot.IE.domain.User;

import com.baloot.IE.domain.Initializer.Initializer;
import com.baloot.IE.repository.User.UserRepository;

import java.util.ArrayList;

public class UserManager {
    private static UserManager instance;
    private final UserRepository repository = UserRepository.getInstance();

    private UserManager() throws Exception {
        Initializer initializer = new Initializer();
        ArrayList<User> users = initializer.getUsersFromAPI("users");
        users.forEach(User::initialize);
        for(User u : users)
            repository.insert(new User(u.getUsername(), u.getPassword(), u.getEmail(), u.getBirthDate(), u.getAddress(), u.getCredit()));
    }
    public static UserManager getInstance() throws Exception {
        if(instance == null)
            instance = new UserManager();
        return instance;
    }
    public User findUserById(String id) {
        User user = null;
        try {
            user = repository.findByField(id, "username");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(user != null)
            return user;
        throw new IllegalArgumentException("User does not exits.");

    }
    public boolean userExists(String username, String password){
        User user;
        try {
            user = repository.findByField(username, "username");
        }
        catch (Exception e) {
            throw new IllegalArgumentException("User does not exits.");
        }
        return user != null && user.isPassEqual(password);
    }

    public ArrayList<User> getAllUsers() {
        try{
            return repository.findAll("");
        }
        catch (Exception e){
            return new ArrayList<>();
        }
    }

    public void addUser(User user) throws Exception {
        User searchUser = null;
        try{
            searchUser = repository.findByField(user.getUsername(), "username");
        }
        catch (Exception e){
            System.out.println("problem in method addUser");
        }
        if(searchUser != null) {
            throw new IllegalArgumentException("Username already exists. Please Login.");
        }
        repository.insert(new User(user.getUsername(), user.getPassword(), user.getEmail(), user.getBirthDate(), user.getAddress(), user.getCredit()));
    }
}
