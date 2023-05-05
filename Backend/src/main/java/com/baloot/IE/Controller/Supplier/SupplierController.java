package com.baloot.IE.Controller.Supplier;

import com.baloot.IE.domain.Product.ProductRepository;
import com.baloot.IE.domain.Supplier.Supplier;
import com.baloot.IE.domain.Supplier.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;

    @Autowired
    public SupplierController() throws Exception {
        supplierRepository = SupplierRepository.getInstance();
        productRepository = ProductRepository.getInstance();
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
