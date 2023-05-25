package com.baloot.IE;

import com.baloot.IE.domain.Product.ProductManager;
import com.baloot.IE.domain.Supplier.SupplierManager;
import com.baloot.IE.domain.User.UserManager;
import com.baloot.IE.repository.Cart.BuyListRepository;
import com.baloot.IE.repository.Cart.CartRepository;
import com.baloot.IE.repository.Cart.PurchaseListRepository;
import com.baloot.IE.repository.Comment.CommentRepository;
import com.baloot.IE.repository.Comment.CommentVoteRepository;
import com.baloot.IE.repository.Product.CategoryRepository;
import com.baloot.IE.repository.Product.ProductRepository;
import com.baloot.IE.repository.Supplier.SupplierRepository;
import com.baloot.IE.repository.User.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BalootApplication {
    public static void main(String[] args) throws Exception {
        SupplierManager.getInstance();
        UserManager.getInstance();
        ProductManager.getInstance();
        CartRepository.getInstance();
        BuyListRepository.getInstance();
        PurchaseListRepository.getInstance();
        CommentRepository.getInstance();
        CommentVoteRepository.getInstance();
        UserManager.initialize();
        SpringApplication.run(BalootApplication.class, args);
    }
}
