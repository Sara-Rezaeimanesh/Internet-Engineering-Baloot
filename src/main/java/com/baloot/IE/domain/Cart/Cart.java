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
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class Cart {
//    private static final AtomicInteger count = new AtomicInteger(0);

    private String username;
    private int cartId;
    private int discount;
    private double total;
    private int no_items;

    private ArrayList<CartItem> buyList;

    private ArrayList<CartItem> purchaseList;
    @JsonIgnore
    private final CartRepository cartRepository = CartRepository.getInstance();
    @JsonIgnore
    private final BuyListRepository buyListRepository = BuyListRepository.getInstance();
    @JsonIgnore
    private final PurchaseListRepository purchaseListRepository = PurchaseListRepository.getInstance();

    public void applyDiscount(String discount) {
        this.discount = Integer.parseInt(discount);
        total = calcTotal();
        cartRepository.update("discount", discount, "username", StringUtility.quoteWrapper(username));
        cartRepository.update("total", String.valueOf(total), "username", StringUtility.quoteWrapper(username));
    }

    public Cart(String username_) {
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
        CartItem cartItem = buyListRepository.findByField(new ArrayList<>(Arrays.asList(String.valueOf(p.getId()), String.valueOf(cartId))), "productId");
        if(cartItem == null)
            buyListRepository.insert(new CartItem(cartId, p, 1));
        else
            buyListRepository.update("quantity", "quantity + 1", String.valueOf(cartItem.getProduct().getId()), String.valueOf(cartItem.getCartId()));
    }

    public void remove(Product p) throws SQLException {
        CartItem ci = buyListRepository.findByField(new ArrayList<>(Arrays.asList(String.valueOf(p.getId()), String.valueOf(cartId))), "productId");
        if(ci != null) {
            ci.updateQuantity(-1);
            if(ci.isOut())
                buyListRepository.delete(String.valueOf(cartId), String.valueOf(ci.getProduct().getId()));
            else
                buyListRepository.update("quantity", String.valueOf(ci.getQuantity()), String.valueOf(ci.getProduct().getId()), String.valueOf(ci.getCartId()));
            total -= p.getPrice();
            cartRepository.update("total", String.valueOf(total), "username", StringUtility.quoteWrapper(username));
            no_items -= 1;
            cartRepository.update("no_items", String.valueOf(no_items), "username", StringUtility.quoteWrapper(username));
            return;
        }
        throw new IllegalArgumentException("Item not available in cart!");
    }

    public float calcTotal(){
        return (float) ((total*(100-discount))/100);
    }

    public void buy() throws SQLException {
        ArrayList<CartItem> buyList = buyListRepository.search(new ArrayList<>(Arrays.asList(String.valueOf(cartId))), "cartId");
        System.out.println(buyList.toString());
        for(CartItem ci : buyList) {
            buyListRepository.delete(String.valueOf(ci.getCartId()), String.valueOf(ci.getProduct().getId()));
            purchaseListRepository.insert(ci);
            ci.updateProductStock();
        }
        cartRepository.delete(this.username);
        cartRepository.insert(new Cart(this.username));
        total = 0;
        no_items = 0;
        cartRepository.update("total", String.valueOf(total), "username", StringUtility.quoteWrapper(username));
        cartRepository.update("no_items", String.valueOf(no_items), "username", StringUtility.quoteWrapper(username));
    }
}
