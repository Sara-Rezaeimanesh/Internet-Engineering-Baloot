package com.baloot.IE.domain.Discount;

import com.baloot.IE.domain.Initializer.Initializer;
import com.baloot.IE.domain.User.User;

import java.util.ArrayList;

public class DiscountRepository {
    private final ArrayList<Discount> discounts;
    private static DiscountRepository instance;

    public DiscountRepository() throws Exception {
        Initializer initializer = new Initializer();
        discounts = initializer.getDiscountsFromAPI("discount");
        discounts.forEach(Discount::initialize);
    }

    public static DiscountRepository getInstance() throws Exception {
        if(instance == null)
            instance = new DiscountRepository();
        return instance;
    }

    public Discount findDiscountById(String did) {
        for(Discount d : discounts)
            if(d.discountCodeEquals(did))
                return d;
        throw new IllegalArgumentException("Discount is not available!");
    }

    public void applyDiscount(String discountCode, User user) throws Exception {
        Discount discount = findDiscountById(discountCode);
        if(discount.isValidToUse(user.getUsername())) {
            user.applyDiscount(discount.getDiscount());
            discount.addToUsed(user.getUsername());
        }
        else{
            throw new Exception("You have already use this discount code.");
        }
    }
}
