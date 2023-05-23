package com.baloot.IE.Controller.User;

import com.baloot.IE.domain.Cart.Cart;
import com.baloot.IE.domain.Discount.DiscountManager;
import com.baloot.IE.domain.Product.Product;
import com.baloot.IE.domain.Product.ProductManager;
import com.baloot.IE.domain.User.User;
import com.baloot.IE.domain.User.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserManager userRepository;
    private final ProductManager productRepository;

    private final DiscountManager discountRepository;

    @Autowired
    public UserController() throws Exception {
        userRepository = UserManager.getInstance();
        productRepository = ProductManager.getInstance();
        discountRepository = DiscountManager.getInstance();
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

    @PostMapping("/{id}/cart/buy")
    public void buyCart(@PathVariable String id) throws Exception {
        userRepository.findUserById(id).pay();
    }

    @PostMapping("/{id}/cart")
    public void addToCart(@PathVariable String id,
                          @RequestBody Map<String, String> body) throws Exception {
        Product product = productRepository.findProductsById(Integer.parseInt(body.get("product-id")));
        userRepository.findUserById(id).getCart().add(product);
    }

    @DeleteMapping("/{id}/cart/{product_id}")
    @ResponseBody
    public void removeFromCart(@PathVariable String id, @PathVariable int product_id) {
        Product product = productRepository.findProductsById(product_id);
        userRepository.findUserById(id).getCart().remove(product);
    }

    @PostMapping("/{id}/discounts")
    public double addDiscount(@PathVariable String id,
                            @RequestBody Map<String, String> body) throws Exception {
        User user = userRepository.findUserById(id);
        return discountRepository.applyDiscount(body.get("discount-id"), user);
    }
}
