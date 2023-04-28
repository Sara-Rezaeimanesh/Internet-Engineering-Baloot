package com.baloot.IE.domain.Product;

import com.baloot.IE.domain.Amazon.Initializer;
import com.baloot.IE.domain.Comment.Comment;
import com.baloot.IE.domain.Rating.Rating;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProductRepository {
    private static ProductRepository instance;
    List<Product> products;

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

    public static HashMap<Product, Float> sortByValue(HashMap<Product, Float> hm)
    {
        HashMap<Product, Float> temp = hm.entrySet().stream()
                .sorted((i1, i2) -> i2.getValue().compareTo(i1.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
        return temp;
    }

    public void setSuggestedProducts(Product product){
        ArrayList<Product> suggestedProducts = new ArrayList<>();
        int id = product.getId();
        HashMap<Product, Float> ratedProduct = new HashMap<>();
        for(Product p : products){
            float score = 0;
            if(id != p.getId())
                for(String category : p.getCategories())
                    if(product.isSameCategory(category))
                        score += 11;
            score += p.getRating();
            ratedProduct.put(p, score);
        }
        HashMap<Product, Float> sortedProduct = sortByValue(ratedProduct);
        int i = 0;
        for (Map.Entry<Product, Float> set : sortedProduct.entrySet()) {
            suggestedProducts.add(set.getKey());
            i ++;
            if(i == 5)
                break;
        }
        product.addSuggestedProducts(suggestedProducts);
    }

}
