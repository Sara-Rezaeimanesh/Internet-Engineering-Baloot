package com.baloot.IE.Controller.User;

import com.baloot.IE.domain.Cart.Cart;
import com.baloot.IE.domain.Product.Product;
import com.baloot.IE.domain.Product.ProductRepository;
import com.baloot.IE.domain.User.User;
import com.baloot.IE.domain.User.UserRepository;
import com.baloot.IE.domain.User.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public UserController() throws Exception {
        userRepository = UserRepository.getInstance();
        productRepository = ProductRepository.getInstance();
    }

    @GetMapping("")
    public List<User> all() {
        return userRepository.getAllUsers();
    }

    @GetMapping("/{id}")
    public User one(@PathVariable String id) {
        return userRepository.findUserById(id);
    }

    @PutMapping("/{id}/credit")
    public void increaseCredit(@PathVariable String id, @RequestBody Map<String, String> body) {
        userRepository.findUserById(id).increaseCredit(Integer.parseInt(body.get("amount")));
    }

    @GetMapping("/{id}/cart")
    public Cart getCart(@PathVariable String id) {
        return userRepository.findUserById(id).getCart();
    }

    @GetMapping("/{id}/cart/buy")
    public void buyCart(@PathVariable String id) throws Exception {
        userRepository.findUserById(id).pay();
    }

    @PostMapping("/{id}/cart")
    public void addToCart(@PathVariable String id,
                          @RequestBody Map<String, String> body) {
        Product product = productRepository.findProductsById(Integer.parseInt(body.get("product-id")));
        userRepository.findUserById(id).getCart().add(product);
    }
}
