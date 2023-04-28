package com.baloot.IE.domain.Cart;

import com.baloot.IE.domain.Product.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

public class Cart {
    private ArrayList<CartItem> buyList;
    private ArrayList<CartItem> purchaseList;
    private int discount;
    private double total;

    public void applyDiscount(String discount) {
        this.discount = Integer.parseInt(discount);
    }

    public Cart() {
        buyList = new ArrayList<>();
        purchaseList = new ArrayList<>();
        total = 0;
    }

    public boolean hasProduct(int commodityId) {
        for(CartItem ci : buyList)
            if(ci.hasProduct(commodityId))
                return true;
        return false;
    }

    public void add(Product p) {
        total += p.getPrice();
        for(CartItem ci : buyList)
            if(ci.hasProduct(p.getId())) {
                ci.updateQuantity(1);
                return;
            }
        buyList.add(new CartItem(p, 1));
    }

    public void remove(Product p) {
        total += p.getPrice();
        for(CartItem ci : buyList)
            if(ci.hasProduct(p.getId())) {
                ci.updateQuantity(-1);
                if(ci.isOut())
                    buyList.remove(ci);
                total -= p.getPrice();
                return;
            }
        throw new IllegalArgumentException("Item not available in cart!");
    }

    public double calcTotal(){
        return (total*(100-discount))/100;
    }

    public void buy() {
        purchaseList.addAll(buyList);
        buyList.clear();
    }
}
