package com.baloot.IE.domain.Discount;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Objects;

@Getter
public class Discount {
    private final String discountCode;
    private final String discount;
    @JsonIgnore
    private ArrayList<String> usedByUsers;

    public Discount(String discountCode, String discount) {
        this.discountCode = discountCode;
        this.discount = discount;
    }

    public void initialize() {
        usedByUsers = new ArrayList<>();
    }

    public boolean discountCodeEquals(String did) {
        return Objects.equals(did, discountCode);
    }

    public String getDiscount() {
        return discount;
    }

    public boolean isValidToUse(String username) {
        return !usedByUsers.contains(username);
    }

    public void addToUsed(String username) {
        usedByUsers.add(username);
    }
}
