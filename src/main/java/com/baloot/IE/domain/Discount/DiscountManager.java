package com.baloot.IE.domain.Discount;

import com.baloot.IE.domain.Initializer.Initializer;
import com.baloot.IE.domain.User.User;
import com.baloot.IE.repository.Discount.DiscountRepository;

import java.sql.SQLException;
import java.util.ArrayList;

public class DiscountManager {
    private static DiscountManager instance;
    private final DiscountRepository discountRepository = DiscountRepository.getInstance();

    public DiscountManager() throws Exception {
        Initializer initializer = new Initializer();
        ArrayList<Discount> discounts = initializer.getDiscountsFromAPI("discount");
        for (Discount d : discounts)
            discountRepository.insert(d);
    }

    public static DiscountManager getInstance() throws Exception {
        if(instance == null)
            instance = new DiscountManager();
        return instance;
    }

    public Discount findDiscountById(String did) throws SQLException {
        Discount d = discountRepository.findByField(did, "discountCode");
        if(d == null) {
            throw new IllegalArgumentException("Discount is not available!");
        }
        return d;
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

    public ArrayList<Discount> getAll() throws SQLException {
        return discountRepository.findAll();
    }
}
