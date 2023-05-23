package com.baloot.IE.domain.Cart;

import com.baloot.IE.domain.Product.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class Cart {
    private static final AtomicInteger count = new AtomicInteger(0);

    private String username;
    private int cartId;
    private ArrayList<CartItem> buyList;
    private ArrayList<CartItem> purchaseList;
    private int discount;
    private double total;
    private int no_items;
    public void initialize() {
        this.cartId = count.incrementAndGet();
    }

    public void applyDiscount(String discount) {
        this.discount = Integer.parseInt(discount);
        total = calcTotal();
    }

    public Cart(String username_) {
        buyList = new ArrayList<>();
        purchaseList = new ArrayList<>();
        total = 0;
        no_items = 0;
        username = username_;
    }

    public Cart(String username_, ArrayList<CartItem> buyList_, ArrayList<CartItem> purchaseList_, int total_, int no_items_) {
        buyList = buyList_;
        purchaseList = purchaseList_;
        total = total_;
        no_items = no_items_;
        username = username_;
    }

    public void add(Product p) throws Exception {
        if(!p.isInStock())
            throw new Exception("Product is not in stock!");
        total += p.getPrice();
        for(CartItem ci : buyList)
            if(ci.hasProduct(p.getId())) {
                ci.updateQuantity(1);
                return;
            }
        buyList.add(new CartItem(p, 1, cartId));
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
        no_items = 0;
    }
}
