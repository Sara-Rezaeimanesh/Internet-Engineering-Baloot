package com.baloot.IE.domain.Discount;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Objects;

@Getter
public class UsedDiscount {
    private final String discountCode;
    private final String username;


    public UsedDiscount(String discountCode, String username) {
        this.discountCode = discountCode;
        this.username = username;
    }

    public boolean isSameDiscountCode(String discountCode) {
        return Objects.equals(discountCode, this.discountCode);
    }
}