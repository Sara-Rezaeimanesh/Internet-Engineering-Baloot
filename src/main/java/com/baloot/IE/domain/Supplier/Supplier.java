package com.baloot.IE.domain.Supplier;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Supplier {

    private int id;
    private String name;
    private String registeryDate;
    private ArrayList<Integer> products = new ArrayList<>();

    public void initialize() {
        products = new ArrayList<>();
    }

    public ArrayList<Integer> getProducts() {
        return products;
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

    public String createHTMLForProvider() {
        return "<ul>\n" +
        "<li id=\"id\">Id:" + this.id +"</li>\n" +
        "<li id=\"name\">Name:" + this.name +"</li>\n" +
        "<li id=\"registryDate\">Registry Date:" + this.registeryDate + "</li>\n"
        + "</ul>";
    }

    public void addProductToSupplierList(Integer p) {
        products.add(p);
    }
}
