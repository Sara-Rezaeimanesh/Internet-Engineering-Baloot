package com.baloot.IE.domain.Cart;

import com.baloot.IE.domain.Product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CartItem {
    private int cartId;
    private Product product;
    private int quantity;


    public boolean hasProduct(int id) {
        return product.getId() == id;
    }
    public void updateQuantity(int value) {
        quantity += value;
    }
    public boolean isOut() {
        return quantity <= 0;
    }

    public void updateProductStock() {
        product.updateStock(-quantity);
    }
}
