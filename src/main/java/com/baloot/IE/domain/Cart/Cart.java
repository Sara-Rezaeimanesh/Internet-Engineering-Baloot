package com.baloot.IE.domain.Cart;

import com.baloot.IE.domain.Product.Product;
import com.baloot.IE.repository.Cart.BuyListRepository;
import com.baloot.IE.repository.Cart.CartRepository;
import com.baloot.IE.repository.Cart.PurchaseListRepository;
import com.baloot.IE.utitlity.StringUtility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class Cart {
    private static final AtomicInteger count = new AtomicInteger(0);

    private String username;
    private int cartId;
    private int discount;
    private double total;
    private int no_items;
    @JsonIgnore
    private final CartRepository cartRepository = CartRepository.getInstance();
    @JsonIgnore
    private final BuyListRepository buyListRepository = BuyListRepository.getInstance();
    @JsonIgnore
    private final PurchaseListRepository purchaseListRepository = PurchaseListRepository.getInstance();

    public void applyDiscount(String discount) {
        this.discount = Integer.parseInt(discount);
        total = calcTotal();
        cartRepository.update("discount", discount, "username", username);
        cartRepository.update("total", String.valueOf(total), "username", username);
    }

    public Cart(String username_) {
        this.cartId = count.incrementAndGet();
        total = 0;
        no_items = 0;
        username = username_;
    }

    public Cart(String username_, int cartId_ , int discount_, int total_, int no_items_) {
        cartId = cartId_;
        total = total_;
        discount = discount_;
        no_items = no_items_;
        username = username_;
    }

    public void add(Product p) throws Exception {
        if(!p.isInStock())
            throw new Exception("Product is not in stock!");
        total += p.getPrice();
        cartRepository.update("total", String.valueOf(total), "username", StringUtility.quoteWrapper(username));
        no_items += 1;
        cartRepository.update("no_items", String.valueOf(no_items), "username", StringUtility.quoteWrapper(username));
        buyListRepository.insert(new CartItem(p, 1, cartId));
    }

    public void remove(Product p) throws SQLException {
        CartItem ci = buyListRepository.findByField(String.valueOf(p.getId()), "productId");
        if(ci != null) {
            ci.updateQuantity(-1);
            if(ci.isOut()) {
                buyListRepository.delete(String.valueOf(cartId), String.valueOf(ci.getProduct().getId()));
            }
            total -= p.getPrice();
            cartRepository.update("total", String.valueOf(total), "username", username);
            no_items -= 1;
            cartRepository.update("no_items", String.valueOf(no_items), "username", username);
            return;
        }
        throw new IllegalArgumentException("Item not available in cart!");
    }

    public float calcTotal(){
        return (float) ((total*(100-discount))/100);
    }

    public void buy() throws SQLException {
        ArrayList<CartItem> buyList = buyListRepository.findAll("cartId = " + cartId);
        System.out.println("hello " + buyList.size());
        for(CartItem ci : buyList) {
            System.out.println(ci.getCartId());
            System.out.println(ci.getProduct().getId());
            buyListRepository.delete(String.valueOf(cartId), String.valueOf(ci.getProduct().getId()));
            purchaseListRepository.insert(ci);
            ci.updateProductStock();
        }
        total = 0;
        no_items = 0;
        cartRepository.update("total", String.valueOf(total), "username", StringUtility.quoteWrapper(username));
        cartRepository.update("no_items", String.valueOf(no_items), "username", StringUtility.quoteWrapper(username));
    }
}
