package com.baloot.IE.domain.Supplier;

import com.baloot.IE.domain.Product.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Supplier {

    private int id;
    private String name;
    private String registeryDate;
    private ArrayList<Product> products = new ArrayList<>();

    public void initialize() {
        products = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public Supplier(@JsonProperty("id") int id,
                    @JsonProperty("name") String name,
                    @JsonProperty("registryDate") String registeryDate) {
        this.id = id;
        this.name = name;
        this.registeryDate = registeryDate;
    }


    public void addProductList(ArrayList<Product> products) {
        this.products = products;
    }
}
