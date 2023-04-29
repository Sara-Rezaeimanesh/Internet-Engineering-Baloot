package com.baloot.IE.domain.Cart;

import com.baloot.IE.domain.Product.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Cart {
    private ArrayList<CartItem> buyList;
    private ArrayList<CartItem> purchaseList;
    private int discount;
    private double total;
    private int no_items;

    public void applyDiscount(String discount) {
        this.discount = Integer.parseInt(discount);
    }

    public Cart() {
        buyList = new ArrayList<>();
        purchaseList = new ArrayList<>();
        total = 0;
        no_items = 0;
    }

    public void add(Product p) throws Exception {
        if(!p.isInStock())
            throw new Exception("Product is not in stock!");
        total += p.getPrice();
        for(CartItem ci : buyList)
            if(ci.hasProduct(p.getId())) {
                ci.updateQuantity(1);
                no_items += 1;
                return;
            }
        buyList.add(new CartItem(p, 1));
        no_items += 1;
    }

    public void remove(Product p) {
        total += p.getPrice();
        for(CartItem ci : buyList)
            if(ci.hasProduct(p.getId())) {
                ci.updateQuantity(-1);
                if(ci.isOut())
                    buyList.remove(ci);
                total -= p.getPrice();
                no_items -= 1;
                return;
            }
        throw new IllegalArgumentException("Item not available in cart!");
    }

    public double calcTotal(){
        return (total*(100-discount))/100;
    }

    public void buy() {
        for(CartItem ci : buyList)
            ci.updateProductStock();

        purchaseList.addAll(buyList);
        buyList.clear();
        total = 0;
    }
}
