package com.baloot.IE.domain.Product;

import com.baloot.IE.domain.Comment.Comment;
import com.baloot.IE.domain.Rating.Rating;
import com.baloot.IE.repository.Cart.PurchaseListRepository;
import com.baloot.IE.repository.Product.ProductRepository;
import com.baloot.IE.repository.Rating.RatingRepository;
import com.baloot.IE.repository.Repository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.SQLException;
import java.util.*;

@Getter
@Setter
@ToString
public class Product {
    private int id;
    private String name;
    private int providerId;
    private int price;
    private ArrayList<String> categories;
    private int inStock;
    private float rating;
    private String image;
    @JsonIgnore
    private ArrayList<Rating> ratings;
    @JsonIgnore
    private ArrayList<Comment> comments;
    @JsonIgnore
    private ArrayList<Product> suggestedProducts;
    @JsonIgnore
    private ProductRepository productRepository = ProductRepository.getInstance();


    public void initialize() {
        ratings = new ArrayList<>();
        comments = new ArrayList<>();
    }

    public void setCategoriesObjects(ArrayList<Category> categories) {
        for(Category c : categories)
            this.categories.add(c.getCategory());
    }

    public Product(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("providerId") int providerId,
                   @JsonProperty("price") int price, @JsonProperty("categories") ArrayList<String> categories, @JsonProperty("rating") float rating,
                   @JsonProperty("inStock") int inStock, @JsonProperty("image") String image) {
        this.id = id;
        this.name = name;
        this.providerId = providerId;
        this.price = price;
        this.rating = rating;
        this.inStock = inStock;
        this.categories = categories;
        this.image = image;
        this.ratings = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public boolean isInStock() {
        return inStock > 0;
    }

    public void updateStock(int i) {
        this.inStock += i;
        productRepository.update("inStock", String.valueOf(inStock), "id", String.valueOf(id));
    }
}
