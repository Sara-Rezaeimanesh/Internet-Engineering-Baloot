package com.baloot.IE.domain.Supplier;

import com.baloot.IE.domain.Product.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Supplier {

    private final int id;
    private final String name;
    private final String registeryDate;
    private ArrayList<Product> products = new ArrayList<>();

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
