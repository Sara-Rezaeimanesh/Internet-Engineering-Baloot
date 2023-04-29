package com.baloot.IE.domain.Discount;

import com.baloot.IE.domain.Amazon.Initializer;

import java.util.ArrayList;

public class DiscountRepository {
    private ArrayList<Discount> discounts;
    private DiscountRepository instance;

    public DiscountRepository() throws Exception {
        Initializer initializer = new Initializer();
        discounts = initializer.getDiscountsFromAPI("discount");
        discounts.forEach(Discount::initialize);
    }

    public DiscountRepository getInstance() throws Exception {
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
}
