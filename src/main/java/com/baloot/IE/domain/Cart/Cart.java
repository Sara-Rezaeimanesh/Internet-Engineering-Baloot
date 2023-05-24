package com.baloot.IE.domain.Cart;

import com.baloot.IE.domain.Product.Product;
import com.baloot.IE.repository.Cart.BuyListRepository;
import com.baloot.IE.repository.Cart.CartRepository;
import com.baloot.IE.repository.Cart.PurchaseListRepository;
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
    private ArrayList<CartItem> buyList;
    private ArrayList<CartItem> purchaseList;
    private int discount;
    private double total;
    private int no_items;

    private final CartRepository cartRepository = CartRepository.getInstance();
    private final BuyListRepository buyListRepository = BuyListRepository.getInstance();
    private final PurchaseListRepository purchaseListRepository = PurchaseListRepository.getInstance();


    public void initialize() {
        this.cartId = count.incrementAndGet();
    }

    public void applyDiscount(String discount) {
        this.discount = Integer.parseInt(discount);
        total = calcTotal();
        cartRepository.update("discount", discount, "username", username);
        cartRepository.update("total", String.valueOf(total), "username", username);
    }

    public Cart(String username_) {
        buyList = new ArrayList<>();
        purchaseList = new ArrayList<>();
        total = 0;
        no_items = 0;
        username = username_;
    }

    public Cart(String username_, ArrayList<CartItem> buyList_, ArrayList<CartItem> purchaseList_, int discount_, int total_, int no_items_) {
        buyList = buyList_;
        purchaseList = purchaseList_;
        total = total_;
        discount = discount_;
        no_items = no_items_;
        username = username_;
    }

    public void add(Product p) throws Exception {
        if(!p.isInStock())
            throw new Exception("Product is not in stock!");
        total += p.getPrice();
        cartRepository.update("total", String.valueOf(total), "username", username);
        for(CartItem ci : buyList)
            if(ci.hasProduct(p.getId())) {
                ci.updateQuantity(1);
                return;
            }
        buyList.add(new CartItem(p, 1, cartId));
        buyListRepository.insert(new CartItem(p, 1, cartId));

        no_items += 1;
        cartRepository.update("no_items", String.valueOf(no_items), "username", username);
    }

    public void remove(Product p) {
        for(CartItem ci : buyList)
            if(ci.hasProduct(p.getId())) {
                ci.updateQuantity(-1);
                if(ci.isOut()) {
                    buyList.remove(ci);
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
        for(CartItem ci : buyList) {
            ci.updateProductStock();
        }

        purchaseList.addAll(buyList);
        for(CartItem pl : buyList)
            purchaseListRepository.insert(pl);
        buyList.clear();

        total = 0;
        no_items = 0;
        cartRepository.update("total", String.valueOf(total), "username", username);
        cartRepository.update("no_items", String.valueOf(no_items), "username", username);
    }
}
