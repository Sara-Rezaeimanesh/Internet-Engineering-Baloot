package com.baloot.IE.Controller.Supplier;

import com.baloot.IE.domain.Product.ProductManager;
import com.baloot.IE.domain.Supplier.Supplier;
import com.baloot.IE.domain.Supplier.SupplierManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/suppliers")
@Order(1)
public class SupplierController {
    private final SupplierManager supplierRepository;
    private final ProductManager productRepository;

    @Autowired
    public SupplierController() throws Exception {
        supplierRepository = SupplierManager.getInstance();
        productRepository = ProductManager.getInstance();
    }

    @GetMapping("")
    public ArrayList<Supplier> all() {
        return supplierRepository.getAll();
    }

    @GetMapping("/{id}")
    public Supplier one(@PathVariable int id) {
        Supplier supplier = supplierRepository.findSupplierById(id);
        supplier.addProductList(productRepository.getProductsBySupplierId(id));
        return supplier;
    }
}
