package com.baloot.IE.domain.User;
import com.baloot.IE.domain.Cart.Cart;
import com.baloot.IE.repository.Cart.CartRepository;
import com.baloot.IE.repository.User.UserRepository;
import com.baloot.IE.utitlity.StringUtility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.util.Objects;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor
public class User {
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private String birthDate;
    private String address;
    private int credit;
    @JsonIgnore
    private final UserRepository repository = UserRepository.getInstance();;
    @JsonIgnore
    private CartRepository cartRepository;

    @JsonIgnore
    private Cart cart;

    public User(@JsonProperty("username") String username_,
                @JsonProperty("password") String password_,
                @JsonProperty("email") String email_,
                @JsonProperty("birthDate") String birthDate_,
                @JsonProperty("address") String address_,
                @JsonProperty("credit")  int credit_) throws Exception {
        if(!Pattern.matches("^[._a-zA-Z0-9]+$", username_))
            throw new Exception("Username cannot contain especial characters.\n");
        this.username = username_;
        this.password = password_;
        this.email = email_;
        this.address = address_;
        this.credit = credit_;
        this.birthDate = birthDate_;
        cartRepository = CartRepository.getInstance();
        this.cart = cartRepository.findByField(this.username,"username");
    }

    public boolean userNameEquals(String username) {
        return Objects.equals(this.username, username);
    }

    private float calculateCurrBuyListPrice(){
        return cart.calcTotal();
    }

    public void increaseCredit(int newCredit) {
        if(newCredit <= 0)
            throw new IllegalArgumentException("Credit value must be greater than zero.");
        credit += newCredit;
        repository.update("credit" ,String.valueOf(credit), "username" , StringUtility.quoteWrapper(this.username));
    }

    public boolean isPassEqual(String password) {
        return Objects.equals(this.password, password);
    }

    public void pay() throws Exception {
        if(credit < calculateCurrBuyListPrice())
            throw new Exception("Credit is not enough");
        credit -= calculateCurrBuyListPrice();
        repository.update("credit" ,String.valueOf(credit),"username" , StringUtility.quoteWrapper(this.username));
        cart.buy();
    }

    public double applyDiscount(String discount) {
        cart.applyDiscount(discount);
        return cart.calcTotal();
    }

    public void initialize()  {
        try {
            Cart cart = null;
            cartRepository = CartRepository.getInstance();
            cart = cartRepository.findByField(this.username,"username");
            if(cart == null) {
                this.cart = new Cart(this.username);
                cartRepository.insert(this.cart);
            }
            else{
                this.cart = cart;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}