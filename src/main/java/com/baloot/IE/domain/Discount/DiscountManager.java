package com.baloot.IE.domain.Discount;

import com.baloot.IE.domain.Initializer.Initializer;
import com.baloot.IE.domain.User.User;

import java.util.ArrayList;

public class DiscountManager {
    private final ArrayList<Discount> discounts;
    private static DiscountManager instance;

    public DiscountManager() throws Exception {
        Initializer initializer = new Initializer();
        discounts = initializer.getDiscountsFromAPI("discount");
        discounts.forEach(Discount::initialize);
    }

    public static DiscountManager getInstance() throws Exception {
        if(instance == null)
            instance = new DiscountManager();
        return instance;
    }

    public Discount findDiscountById(String did) {
        for(Discount d : discounts)
            if(d.discountCodeEquals(did))
                return d;
        throw new IllegalArgumentException("Discount is not available!");
    }

    public double applyDiscount(String discountCode, User user) throws Exception {
        Discount discount = findDiscountById(discountCode);
        double totalAfterDiscount;
        if(discount.isValidToUse(user.getUsername())) {
            totalAfterDiscount = user.applyDiscount(discount.getDiscount());
            discount.addToUsed(user.getUsername());
        }
        else{
            throw new Exception("You have already use this discount code.");
        }
        return totalAfterDiscount;
    }

    public ArrayList<Discount> getAll() {
        return discounts;
    }
}
