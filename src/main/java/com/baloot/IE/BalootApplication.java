package com.baloot.IE;

import com.baloot.IE.domain.Product.ProductManager;
import com.baloot.IE.domain.Supplier.SupplierManager;
import com.baloot.IE.domain.User.UserManager;
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

        SpringApplication.run(BalootApplication.class, args);
    }
}
