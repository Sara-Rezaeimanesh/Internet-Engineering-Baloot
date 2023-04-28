package com.baloot.IE.Controller.CartController;

import com.baloot.IE.domain.Session.Session;
import com.baloot.IE.domain.Cart.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {
    private Cart cart;
    private Session session;

    @Autowired
    public CartController() {
        session = Session.getInstance();
    }
    @GetMapping("")
    public Cart getCart() {
        return session.getActiveUser().getCart();
    }

    @GetMapping("/buy")
    public void buyCart() {
        cart = session.getActiveUser().getCart();
        cart.buy();
    }
}
