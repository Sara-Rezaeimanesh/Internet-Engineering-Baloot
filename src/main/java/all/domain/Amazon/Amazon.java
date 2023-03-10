package all.domain.Amazon;

import all.domain.Comment.Comment;
import all.domain.Rating.Rating;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import all.domain.Product.Product;
import all.domain.Supplier.Supplier;
import all.domain.User.User;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Amazon {
    private final String PRODUCT_ALREADY_EXIST_ERROR = "Product already exista!";
    private final String SUPPLIER_DOES_NOT_EXIST_ERROR = "Supplier is not exist!";
    private final String PRODUCT_DOES_NOT_EXIT_ERROR = "Product does not exist!";
    private final String USER_DOES_NOT_EXIST_ERROR = "User does not exist!";

    private ArrayList<User> users;

    private ArrayList<Supplier> suppliers;

    private ArrayList<Product> products;

    public generateCoursesPageHTML(){

    }


    public Amazon() throws Exception {
        Initializer initializer = new Initializer();
        suppliers = initializer.getProvidersFromAPI("providers");
        for(Supplier s : suppliers)
            System.out.println(s.getId());
        users = initializer.getUsersFromAPI("users");
        products = initializer.getCommoditiesFromAPI("commodities");
        ArrayList<Comment> comments = initializer.getCommentsFromAPI("comments");
        for(Comment c : comments){
            Product p = findProductsById(c.getCommodityId());
            assert p != null;
            p.addComment(c);
        }
    }

    private boolean isInSuppliers(int id) {
        for (Supplier s : suppliers)
            if (id == s.getId())
                return true;
        return false;
    }

    private Product findProductsById(int id) {
        for (Product p : products)
            if (id == p.getId())
                return p;
        return null;
    }

    private User findUserById(String uid) {
        for(User u : users)
            if(u.userNameEquals(uid))
                return u;
        return null;
    }

    public void addUser(User user) {
        for (User user_ : this.users)
            if (user_.userNameEquals(user.getUsername())) {
                user_.updateUserInfo(user);
            }
        users.add(user);
    }

    public void addProduct(Product product) throws Exception {
        if (!isInSuppliers(product.getProviderId()))
            throw new Exception(SUPPLIER_DOES_NOT_EXIST_ERROR);
        if (findProductsById(product.getId()) != null)
            throw new Exception(PRODUCT_ALREADY_EXIST_ERROR);
        products.add(product);
    }

    public void addSupplier(Supplier supplier) {
        suppliers.add(supplier);
    }

    public void listCommodities() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        System.out.println("\"data\": {\"commoditiesList\": " + ow.writeValueAsString(products) + "}");
    }

    public void rateCommodity(Rating rating) throws Exception {
        if (findUserById(rating.getUsername()) == null)
            throw new Exception(USER_DOES_NOT_EXIST_ERROR);
        Product p = findProductsById(rating.getProductId());
        if(p != null) p.updateRating(rating);
        else throw new Exception(PRODUCT_DOES_NOT_EXIT_ERROR);
    }

    public void getCommodityById(Integer id) throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Product p = findProductsById(id);
        if(p != null) System.out.println("\"data\": " + ow.writeValueAsString(p));
        else throw new Exception(PRODUCT_DOES_NOT_EXIT_ERROR);
    }

    public void getCommoditiesByCategory(String category) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ArrayList<Product> sameCat = new ArrayList<>();
        for (Product p : products) {
                if (p.isSameCategory(category))
                    sameCat.add(p);
        }
        System.out.println("\"data\": {\"commoditiesListByCategory\": " + ow.writeValueAsString(sameCat) + "}");
    }

    public void addToBuyList(String username, int commodityId) throws Exception {
        Product p = findProductsById(commodityId);
        User u = findUserById(username);
        if(p == null) throw new Exception(PRODUCT_DOES_NOT_EXIT_ERROR);
        if(u == null) throw new Exception(USER_DOES_NOT_EXIST_ERROR);
        if(u.hasBoughtProduct(commodityId))
            throw  new Exception("Product already added.");
        if(!p.isInStock()) throw new Exception("Product is not in stock!");
        u.addProduct(p);
        p.updateStock(-1);
    }

    public void removeFromBuyList(String username, int commodityId) throws Exception {
        Product p = findProductsById(commodityId);
        User u = findUserById(username);
        if(p == null) throw new Exception(PRODUCT_DOES_NOT_EXIT_ERROR);
        if(u == null) throw new Exception(USER_DOES_NOT_EXIST_ERROR);
        if(!u.hasBoughtProduct(commodityId))
            throw  new Exception("Product does not exist in buyList!");

        u.removeProduct(p);
        p.updateStock(1);
    }

    public void getUserBuyList(String name) throws Exception {
        User u = findUserById(name);
        if(u == null) throw new Exception(USER_DOES_NOT_EXIST_ERROR);
        u.printBuyList();
    }
}
