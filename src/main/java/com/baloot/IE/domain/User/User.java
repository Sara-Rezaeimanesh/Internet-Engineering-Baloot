package com.baloot.IE.domain.User;
import com.baloot.IE.domain.Product.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor
public class User {
    private String username;
    private String password;
    private String email;
    private String birthDate;
    private String address;
    private int credit;
    private int discount;
    private ArrayList<Product> buyList;
    private ArrayList<Product> purchaseList;

    public void initialize() {
        buyList = new ArrayList<>();
        purchaseList = new ArrayList<>();
    }

    public void updateUserInfo(User newUserInfo){
        username = newUserInfo.username;
        password = newUserInfo.password;
        address = newUserInfo.address;
        birthDate = newUserInfo.birthDate;
        credit = newUserInfo.credit;
        email = newUserInfo.email;
    }

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
        this.buyList = new ArrayList<>();
        this.purchaseList = new ArrayList<>();
    }

    public boolean hasBoughtProduct(int commodityId) {
        for(Product p : buyList)
            if(p.getId() == commodityId)
                return true;
        return false;
    }

    public void addProduct(Product p) { buyList.add(p); }

    public boolean userNameEquals(String username) {
        return Objects.equals(this.username, username);
    }

    public void printBuyList() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        System.out.println("\"data\": {\"buyList\": " + ow.writeValueAsString(buyList) + "}");

    }

    public void removeProduct(Product p) { buyList.remove(p); }

    private int calculateCurrBuyListPrice(){
        int price = 0;
        for(Product p : buyList)
            price += p.getPrice();
        return (price*(100-discount))/100;
    }

    public void increaseCredit(int newCredit) {
        credit += newCredit;
    }

    public boolean isPassEqual(String password) {
        return Objects.equals(this.password, password);
    }

    public void payBuyList() throws Exception {
        if(credit < calculateCurrBuyListPrice())
            throw new Exception("Credit is not enough");
        credit -= calculateCurrBuyListPrice();
        purchaseList.addAll(buyList);
        buyList.clear();
    }

    public void applyDiscount(String discount) {
        this.discount = Integer.parseInt(discount);
    }
}