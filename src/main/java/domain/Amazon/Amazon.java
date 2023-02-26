package domain.Amazon;
import domain.Product.Product;
import domain.User.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Provider;
import java.util.ArrayList;

public class Amazon {
    private ArrayList<User> users;
    private ArrayList<Provider> provider;
    private ArrayList<Product> products;

    public void addUser(User user){
        users.add(user);
    }

}
