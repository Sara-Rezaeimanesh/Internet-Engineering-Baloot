package com.baloot.IE.Controller.User;

import com.baloot.IE.domain.Cart.Cart;
import com.baloot.IE.domain.Discount.DiscountManager;
import com.baloot.IE.domain.Product.Product;
import com.baloot.IE.domain.Product.ProductManager;
import com.baloot.IE.domain.User.User;
import com.baloot.IE.domain.User.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Order(2)
public class UserController {
    private final UserManager userManager;
    private final ProductManager productManager;
    private final DiscountManager discountManager;

    @Autowired
    public UserController() throws Exception {
        userManager = UserManager.getInstance();
        productManager = ProductManager.getInstance();
        discountManager = DiscountManager.getInstance();
    }

    @GetMapping("")
    public List<User> all() {
        return userManager.getAllUsers();
    }

    @GetMapping("/{id}")
    public User one(@PathVariable String id) {
        return userManager.findUserById(id);
    }

    @PutMapping("/{id}/credit")
    public void increaseCredit(@PathVariable String id, @RequestBody Map<String, String> body) {
        userManager.findUserById(id).increaseCredit(Integer.parseInt(body.get("amount")));
    }

    @GetMapping("/{id}/cart")
    public Cart getCart(@PathVariable String id) throws SQLException {
        return userManager.findUserById(id).getCart();
    }

    @PostMapping("/{id}/cart/buy")
    public void buyCart(@PathVariable String id) throws Exception {
        userManager.findUserById(id).pay();
    }

    @PostMapping("/{id}/cart")
    public int addToCart(@PathVariable String id,
                          @RequestBody Map<String, String> body) throws Exception {
        Product product = productManager.findProductsById(Integer.parseInt(body.get("product-id")));
        Cart cart = userManager.findUserById(id).getCart();
        cart.add(product);
        return cart.getNo_items();
    }

    @DeleteMapping("/{id}/cart/{product_id}")
    @ResponseBody
    public int removeFromCart(@PathVariable String id, @PathVariable int product_id) throws SQLException {
        Product product = productManager.findProductsById(product_id);
        Cart cart = userManager.findUserById(id).getCart();
        cart.remove(product);
        return cart.getNo_items();
    }

    @PostMapping("/{id}/discounts")
    public double addDiscount(@PathVariable String id,
                            @RequestBody Map<String, String> body) throws Exception {
        User user = userManager.findUserById(id);
        return discountManager.applyDiscount(body.get("discount-id"), user);
    }
}
