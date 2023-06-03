package com.baloot.IE.domain.Product;

import com.baloot.IE.domain.Cart.CartItem;
import com.baloot.IE.domain.Comment.CommentManager;
import com.baloot.IE.domain.Initializer.Initializer;
import com.baloot.IE.domain.Comment.Comment;
import com.baloot.IE.domain.Rating.Rating;
import com.baloot.IE.domain.User.UserManager;
import com.baloot.IE.repository.Product.CategoryRepository;
import com.baloot.IE.repository.Product.ProductRepository;
import com.baloot.IE.repository.Rating.RatingRepository;
import com.baloot.IE.utitlity.StringUtility;
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
    private final UserManager userManager = UserManager.getInstance();

    private final RatingRepository ratingRepository = RatingRepository.getInstance();
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
            searchString = "p inner join CATEGORIES c on p.id = c.productId\nwhere c.category = \""+category+"\"";
        if (priceRange != null) {
            String[] priceRangeArray = priceRange.split("-");
            try {
                Integer.parseInt(priceRangeArray[0]);
                Integer.parseInt(priceRangeArray[1]);
            } catch (Exception e) {
                throw new IllegalArgumentException("Bad values for price range for search in products");
            }
            searchString += (searchString.equals("")) ? "\nwhere " : " and ";
            searchString += "price < " + priceRangeArray[1] + " and " + "price > " + priceRangeArray[0];
        }
        if (name != null) {
            searchString += (searchString.equals("")) ? "\nwhere " : " and ";
            searchString += "LOWER("+ StringUtility.quoteWrapper(name)+") LIKE CONCAT('%', LOWER(name), '%')";
        }
        if(id != null) {
            searchString += (searchString.equals("")) ? "\nwhere " : " and ";
            try {
                Integer.parseInt(id);
            } catch (Exception e) {
                throw new IllegalArgumentException("Bad values for id for search in products");
            }
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
        return repository.search("",searchString);
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
            p.setCategoriesObjects(categoryRepository.search(String.valueOf(id), "productId"));
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

    public void voteComment(String userId, int productId, int commentId, int vote) throws SQLException {
        String userEmail = userManager.findUserById(userId).getEmail();
        commentManager.voteComment(userEmail, commentId, vote);
    }

    public void updateRating(String username, String quantity, int id) throws Exception {
        System.out.println("hi hi");
        Rating rating = new Rating(username, id, Integer.parseInt(quantity));
        Product product = findProductsById(id);
        ArrayList<Rating> r = ratingRepository.search(new ArrayList<>(Arrays.asList(username, String.valueOf(product.getId()))), "");
        if(r.size() == 0)
            ratingRepository.insert(rating);
        else
            ratingRepository.update("rating", String.valueOf(quantity), "", new ArrayList<>(Arrays.asList(username, String.valueOf(product.getId()))));
        product.setRating(ratingRepository.calculateRating(id));
        System.out.println("rating is: " + ratingRepository.calculateRating(id));
        repository.update("rating", String.valueOf(rating.getScore()), "id", String.valueOf(id));
    }

    public void addComment(String userId, int id, String commentTxt) throws Exception {
        String todayDate = getTodayDate();
        String userEmail = userManager.findUserById(userId).getEmail();
        Comment comment = new Comment(userEmail, id, commentTxt, todayDate);
        commentManager.addComment(comment);
    }

    private String getTodayDate() {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(formatter);
    }

    public ArrayList<Comment> getProductComments(int id) {
        return commentManager.searchComments(String.valueOf(id), "commodityId");
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

    public ArrayList<Product> getSuggestedProducts(Product product) throws SQLException {
        int id = product.getId();
        return repository.executeQuery("SELECT *, (CASE WHEN exists (select * \n" +
                                            "from categories c\n" +
                                            "where c.productId = p.id and c.category in" +
                                            "(select c1.category from categories c1 where c1.productId = "+id+")) \n" +
                                            "THEN 11+CAST(p.rating AS FLOAT) ELSE CAST(p.rating AS FLOAT) END)" +
                                            "as score FROM products p\n" +
                                            "where p.id != "+id+"\n" +
                                            "ORDER BY score desc\n" +
                                            "limit 5;");
    }
}
