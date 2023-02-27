package all.domain.User;
import all.domain.Product.Product;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class User {
    private String username;
    private String password;
    private String email;
    private String BirthDate;
    private String address;
    private int credit;

    private ArrayList<Product> products;

    public User(String username_, String password_, String email_,
                String birthDate_, String address_, int credit_) throws Exception {
        if(!Pattern.matches("^[._a-zA-Z0-9]+$", username_))
            throw new Exception("Username cannot contain especial characters.\n");
        this.username = username_;
        this.password = password_;
        this.email = email_;
        this.address = address_;
        this.credit = credit_;
        this.products = new ArrayList<>();
    }

    public void addProduct(Product p) { products.add(p); }
}
