package com.baloot.IE.domain.Product;

import com.baloot.IE.domain.Amazon.Initializer;
import com.baloot.IE.domain.Comment.Comment;
import com.baloot.IE.domain.Rating.Rating;
import com.baloot.IE.domain.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProductRepository {
    private static ProductRepository instance;
    List<Product> products;
    private Product chosenProduct;

    public ProductRepository() throws Exception {
        Initializer initializer = new Initializer();
        products = initializer.getCommoditiesFromAPI("commodities");
        products.forEach(Product::initialize);
        ArrayList<Comment> comments = initializer.getCommentsFromAPI("comments");
        comments.forEach(Comment::initialize);
        for(Comment c : comments){
            Product p = findProductsById(c.getCommodityId());
            if(p == null) continue;
            p.addComment(c);
        }
    }


    public List<Product> filterProducts(String category, String priceRange, String name, String id) {
        List<Product> searchResults = new ArrayList<>(products);
        if (category != null)
            searchResults = searchResults.stream().filter(product -> product.getCategories().contains(category)).collect(Collectors.toList());
        if (priceRange != null) {
            String[] priceRangeArray = priceRange.split("-");
            System.out.println(priceRangeArray[0] + " " + priceRangeArray[1]);
            double minPrice = Double.parseDouble(priceRangeArray[0]);
            double maxPrice = Double.parseDouble(priceRangeArray[1]);
            searchResults = searchResults.stream()
                    .filter(product -> product.getPrice() >= minPrice && product.getPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }
        if (name != null)
            searchResults = searchResults.stream().filter(product -> product.getName().contains(name)).collect(Collectors.toList());
        if(id != null)
            searchResults = searchResults.stream().filter(product -> product.getId() == Integer.parseInt(id)).collect(Collectors.toList());
        if(searchResults.size() == 0)
            throw new IllegalArgumentException("No items matched your search.");

        return searchResults;
    }

    public List<Product> sortProducts(List<Product> products, String sort_param) {
        if(Objects.equals(sort_param, "price"))
            products.sort(Comparator.comparingDouble(Product::getPrice));
        else if(Objects.equals(sort_param, "rate"))
            products.sort(Comparator.comparingDouble(Product::getRating));
        else {
            throw new IllegalArgumentException("The requested search method is currently not available. Please try a different search method.");
        }
        return products;
    }

    public static ProductRepository getInstance() throws Exception {
        if(instance == null)
            instance = new ProductRepository();
        return instance;
    }

    public Product findProductsById(int id) {
        for (Product p : products)
            if (id == p.getId())
                return p;
        throw new IllegalArgumentException("Product does not exist!");
    }

    public void add(Product product) {
        products.add(product);
    }

    public void voteComment(String userEmail, int productId, int commentId, int vote) {
        Product p = findProductsById(productId);
        p.voteComment(userEmail, commentId, vote);
    }

    public void saveChosenProduct(Product p) {
        chosenProduct = p;
    }

    public void updateRating(String username, String quantity, int id) throws Exception {
        Rating rating = new Rating(username, id, Integer.parseInt(quantity));
        Product product = findProductsById(id);
        product.updateRating(rating);
    }

    public void addComment(String userEmail, int id, String commentTxt) {
        String todayDate = getTodayDate();
        Comment comment = new Comment(userEmail, id, commentTxt, todayDate);
        Product product = findProductsById(id);
        product.addComment(comment);
    }

    private String getTodayDate() {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(formatter);
    }

    public ArrayList<Comment> getProductComments(int id) {
        Product p = findProductsById(id);
        return p.getComments();
    }
}
