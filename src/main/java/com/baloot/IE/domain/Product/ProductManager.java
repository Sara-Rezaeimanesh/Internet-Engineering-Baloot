package com.baloot.IE.domain.Product;

import com.baloot.IE.domain.Comment.CommentManager;
import com.baloot.IE.domain.Initializer.Initializer;
import com.baloot.IE.domain.Comment.Comment;
import com.baloot.IE.domain.Rating.Rating;
import com.baloot.IE.domain.Supplier.Supplier;
import com.baloot.IE.repository.Product.CategoryRepository;
import com.baloot.IE.repository.Product.ProductRepository;
import com.baloot.IE.repository.Supplier.SupplierRepository;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProductManager {
    private static ProductManager instance;
    List<Product> products;
    private final ProductRepository repository = ProductRepository.getInstance();
    private final CommentManager commentManager = CommentManager.getInstance();
    private final CategoryRepository categoryRepository = CategoryRepository.getInstance();

    public ProductManager() throws Exception {
        Initializer initializer = new Initializer();
        products = initializer.getCommoditiesFromAPI("v2/commodities");
        products.forEach(Product::initialize);
        for(Product p : products)
            repository.insert(p);
        for(Product p : products)
            for(String cat : p.getCategories())
                categoryRepository.insert(new Category(p.getId(), cat));
    }

    // TODO
    public List<Product> filterProducts(String category, String priceRange, String name, String id, int supplier_id, String available) throws SQLException {
        List<Product> searchResults = new ArrayList<>(products);
        String searchString = "";
        if (category != null)
            searchString = "p inner join CATEGORIES c on p.id = c.productId\nwhere c.category = "+category;
        if (priceRange != null) {
            String[] priceRangeArray = priceRange.split("-");
            searchString += (searchString.equals("")) ? "\nwhere " : " and ";
            searchString += "price < " + priceRangeArray[1] + " and " + "price > " + priceRangeArray[0];
        }
        if (name != null) {
            searchString += (searchString.equals("")) ? "\nwhere " : " and ";
            searchString += "LOWER("+name+") LIKE CONCAT('%', LOWER(name), '%')";
        }
        if(id != null) {
            searchString += (searchString.equals("")) ? "\nwhere " : " and ";
            searchString += "id = " + id;
        }
        if(searchResults.size() == 0)
            throw new IllegalArgumentException("No items matched your search.");
        if(supplier_id != -1) {
            searchString += (searchString.equals("")) ? "\nwhere " : " and ";
            searchString += "providerId = " + supplier_id;
        }
        if(available != null) {
            searchString += (searchString.equals("")) ? "\nwhere " : " and ";
            searchString += "inStock > 0";
        }
        return repository.findAll(searchString);
    }

    // TODO
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

    public static ProductManager getInstance() throws Exception {
        if(instance == null)
            instance = new ProductManager();
        return instance;
    }

    public Product findProductsById(int id) throws SQLException {
        Product p = null;
        try {
            p = repository.findByField(String.valueOf(id), "id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(p != null) {
            p.setCategoriesObjects(categoryRepository.findAll("productId = " + id));
            return p;
        }

        throw new IllegalArgumentException("Product does not exist!");
    }

    public void add(Product product) {
        try {
            repository.insert(product);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void voteComment(String userEmail, int productId, int commentId, int vote) throws SQLException {
        commentManager.voteComment(userEmail, commentId, vote);
    }

    public void updateRating(String username, String quantity, int id) throws Exception {
        Rating rating = new Rating(username, id, Integer.parseInt(quantity));
        Product product = findProductsById(id);
        product.updateRating(rating);
    }

    public void addComment(String userEmail, int id, String commentTxt) throws Exception {
        String todayDate = getTodayDate();
        Comment comment = new Comment(userEmail, id, commentTxt, todayDate);
        commentManager.addComment(comment);
    }

    private String getTodayDate() {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(formatter);
    }

    public ArrayList<Comment> getProductComments(int id) {
        return commentManager.getAllComments("commodityId = " + id);
    }

    public ArrayList<Product> getProductsBySupplierId(int supplierId) {
        ArrayList<Product> supplierProducts = new ArrayList<>();
        for(Product product  : products)
            if(product.getProviderId() == supplierId)
                supplierProducts.add(product);
        return supplierProducts;
    }

    public static HashMap<Product, Float> sortByValue(HashMap<Product, Float> hm)
    {
        return hm.entrySet().stream()
                .sorted((i1, i2) -> i2.getValue().compareTo(i1.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    public void setSuggestedProducts(Product product){
//        ArrayList<Product> suggestedProducts = new ArrayList<>();
//        int id = product.getId();
//        HashMap<Product, Float> ratedProduct = new HashMap<>();
//        for(Product p : products){
//            float score = 0;
//            if(id != p.getId())
//                for(String category : p.getCategories())
//                    if(product.isSameCategory(category))
//                        score += 11;
//            score += p.getRating();
//            ratedProduct.put(p, score);
//        }
//        HashMap<Product, Float> sortedProduct = sortByValue(ratedProduct);
//        int i = 0;
//        for (Map.Entry<Product, Float> set : sortedProduct.entrySet()) {
//            if(product.getId() != set.getKey().getId()) {
//                suggestedProducts.add(set.getKey());
//                i ++;
//            }
//            if(i == 5)
//                break;
//        }
//        product.addSuggestedProducts(suggestedProducts);
    }
}
