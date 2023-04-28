package com.baloot.IE.Controller.CartController;

import com.baloot.IE.Controller.User.UserController;
import com.baloot.IE.domain.Session.Session;
import com.baloot.IE.domain.Cart.Cart;
import com.baloot.IE.domain.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("users/{id}/cart")
public class CartController {
    private final UserRepository userRepository;

    @Autowired
    public CartController() throws Exception {
        userRepository = UserRepository.getInstance();
    }

    @GetMapping("")
    public Cart getCart(@PathVariable String id) {
        return userRepository.findUserById(id).getCart();
    }

    @GetMapping("/buy")
    public void buyCart(@PathVariable String id) {
        Cart cart = userRepository.findUserById(id).getCart();
        cart.buy();
    }
}
