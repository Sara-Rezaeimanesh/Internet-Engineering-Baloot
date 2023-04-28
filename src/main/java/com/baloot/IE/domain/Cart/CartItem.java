package com.baloot.IE.domain.Cart;

import com.baloot.IE.domain.Product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CartItem {
    private Product product;
    private int quantity;

    public double calcTotal() {
        return quantity*product.getPrice();
    }
    public boolean hasProduct(int id) {
        return product.getId() == id;
    }
    public void updateQuantity(int value) {
        quantity += value;
    }

    public boolean isOut() {
        return quantity == 0;
    }
}
