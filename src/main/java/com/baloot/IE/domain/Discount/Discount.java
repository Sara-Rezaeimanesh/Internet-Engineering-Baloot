package com.baloot.IE.domain.Discount;

import com.baloot.IE.repository.Discount.UsedDiscountRepository;
import com.baloot.IE.utitlity.StringUtility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@Getter
public class Discount {
    private final String discountCode;
    private final String discount;

    private final UsedDiscountRepository usedDiscountRepository = UsedDiscountRepository.getInstance();

    public Discount(String discountCode, String discount) {
        this.discountCode = discountCode;
        this.discount = discount;
    }

    public boolean discountCodeEquals(String did) {
        return Objects.equals(did, discountCode);
    }

    public String getDiscount() {
        return discount;
    }

    public boolean isValidToUse(String username) throws SQLException {
        ArrayList<UsedDiscount> usedDiscount = usedDiscountRepository.search(new ArrayList<>(Arrays.asList(username, discountCode)), "alaki");
        return usedDiscount.size() == 0;
    }

    public void addToUsed(String username) throws SQLException {
        usedDiscountRepository.insert(new UsedDiscount(this.discountCode, username));
    }
}
