package com.baloot.IE.domain.Product;

import com.baloot.IE.domain.Comment.Comment;
import com.baloot.IE.domain.Rating.Rating;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;

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
    @JsonIgnore
    private ArrayList<Rating> ratings;
    @JsonIgnore
    private ArrayList<Comment> comments;
    @JsonIgnore
    private ArrayList<Product> suggestedProducts;

    public void initialize() {
        ratings = new ArrayList<>();
        comments = new ArrayList<>();
    }

    public Product(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("providerId") int providerId,
                   @JsonProperty("price") int price, @JsonProperty("categories") ArrayList<String> categories, @JsonProperty("rating") int rating,
                   @JsonProperty("inStock") int inStock) {
        this.id = id;
        this.name = name;
        this.providerId = providerId;
        this.price = price;
        this.categories = categories;
        this.rating = rating;
        this.inStock = inStock;
        this.ratings = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public void addComment(Comment comment){
        if(comments == null)
            comments = new ArrayList<>();
        comments.add(comment);
    }

    public void updateRating(Rating newRating) {
        System.out.println(newRating.getScore());
        float sumRating = ratings.size() * rating;
        boolean alreadyRated = false;
        for (Rating r : this.ratings)
            if (Objects.equals(newRating.getUsername(), r.getUsername())) {
                float diff = newRating.getScore() - r.getScore();
                r.setScore(newRating.getScore());
                sumRating = sumRating + diff;
                alreadyRated = true;
            }
        if (!alreadyRated) {
            sumRating = sumRating + newRating.getScore();
            ratings.add(newRating);
        }
        setRating(sumRating / ratings.size());
    }

    public boolean isInStock() {
        return inStock > 0;
    }

    public void updateStock(int i) {
        this.inStock += i;
    }

    private String createCatString() {
        String catString = "";
        for(int i = 0; i < categories.size(); i++)
            catString += i + 1 == categories.size() ? categories.get(i) : categories.get(i) + ", ";
        return catString;
    }

    public void voteComment(String userEmail, int commentId, int vote) {
        for(Comment c : comments)
            if(c.idMatches(commentId))
                c.updateVote(userEmail, vote);
    }

    public boolean isSameCategory(String category) {
        return categories.contains(category);
    }

    public void addSuggestedProducts(ArrayList<Product> suggestedProducts) {
        this.suggestedProducts = suggestedProducts;
    }
}