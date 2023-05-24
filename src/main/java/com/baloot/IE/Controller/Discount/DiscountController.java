package com.baloot.IE.Controller.Discount;

import com.baloot.IE.domain.Discount.Discount;
import com.baloot.IE.domain.Discount.DiscountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/discounts")
public class DiscountController {
    private final DiscountManager discountManager;

    @Autowired
    public DiscountController() throws Exception {
        discountManager = DiscountManager.getInstance();
    }

    @GetMapping("")
    public ArrayList<Discount> all() throws SQLException {
        return discountManager.getAll();
    }
}
