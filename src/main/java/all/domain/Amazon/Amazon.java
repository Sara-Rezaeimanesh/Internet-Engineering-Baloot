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
import com.google.common.io.Resources;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Amazon {
    private final String PRODUCT_ALREADY_EXIST_ERROR = "Product already exista!";
    private final String SUPPLIER_DOES_NOT_EXIST_ERROR = "Supplier is not exist!";
    private final String PRODUCT_DOES_NOT_EXIT_ERROR = "Product does not exist!";
    private final String USER_DOES_NOT_EXIST_ERROR = "User does not exist!";

    private ArrayList<User> users = new ArrayList<>();

    private ArrayList<Supplier> suppliers = new ArrayList<>();

    private ArrayList<Product> products = new ArrayList<>();

    public Amazon() throws Exception {
//        Initializer initializer = new Initializer();
//        suppliers = initializer.getProvidersFromAPI("providers");
//        for(Supplier s : suppliers)
//            System.out.println(s.getId());
//        users = initializer.getUsersFromAPI("users");
//        products = initializer.getCommoditiesFromAPI("commodities");
//        ArrayList<Comment> comments = initializer.getCommentsFromAPI("comments");
//        for(Comment c : comments){
//            Product p = findProductsById(c.getCommodityId());
//            assert p != null;
//            p.addComment(c);
//        }
        Product product = new Product(1, "ice cream", 1, 20000, new ArrayList<>(){{add("snack");}}, 10, 1);
        User user = new User("user1", "#123", "a@gmail.com", "12/5/2022", "hi", 500);
        User user2 = new User("user2", "#123", "a@gmail.com", "12/5/2022", "hi", 500);
        products.add(product);
        users.add(user);
        users.add(user2);
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

    public String rateCommodity(Rating rating) throws Exception {
        if (findUserById(rating.getUsername()) == null)
            return readHTMLPage("404.html");
        Product p = findProductsById(rating.getProductId());
        if(p != null) {
            p.updateRating(rating);
            return "success";
        }
        else return readHTMLPage("404.html");
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

    public String addToBuyList(String username, int commodityId) throws Exception {
        Product p = findProductsById(commodityId);
        User u = findUserById(username);
        if(p == null || u == null)
            return readHTMLPage("404.html");
        if(u.hasBoughtProduct(commodityId))
            return readHTMLPage("403.html");
        if(!p.isInStock()) throw new Exception("Product is not in stock!");
        u.addProduct(p);
        p.updateStock(-1);

        return "success";
    }

    public String removeFromBuyList(String username, int commodityId) throws Exception {
        Product p = findProductsById(commodityId);
        User u = findUserById(username);
        if(p == null || u == null)
            return readHTMLPage("404.html");
        if(!u.hasBoughtProduct(commodityId))
            return readHTMLPage("403.html");
        u.removeProduct(p);
        p.updateStock(1);

        return "success";
    }

    public void getUserBuyList(String name) throws Exception {
        User u = findUserById(name);
        if(u == null) throw new Exception(USER_DOES_NOT_EXIST_ERROR);
        u.printBuyList();
    }

    public String createCommoditiesPage() throws Exception {
        String commoditiesHTML = readHTMLPage("Commodities_start.html");
        for(Product p : products)
            commoditiesHTML += p.createHTML();
        commoditiesHTML += readHTMLPage("Commodities_end.html");

        return commoditiesHTML;
    }

    private String readHTMLPage(String fileName) throws Exception {
        File file = new File(Resources.getResource("templates/" + fileName).toURI());
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    public String createCommodityPage(String id) throws Exception {
        String commodityHTML = readHTMLPage("Commodity_start.html");
        Product p = findProductsById(Integer.parseInt(id));
        if(p == null)
            return readHTMLPage("404.html");

        commodityHTML += p.createHTMLForCommodity();
        commodityHTML += "<form action=\"/rateCommodity/"+ id +"\" method=\"POST\">\n" +
                "    <label>Your ID:</label>\n" +
                "    <input type=\"text\" name=\"user_id\" value=\"\" />\n" +
                "    <br><br>\n" +
                "    <label>Rate(between 1 and 10):</label>\n" +
                "    <input type=\"number\" id=\"quantity\" name=\"quantity\" min=\"1\" max=\"10\">\n" +
                "    <button type=\"submit\">Rate</button>\n" +
                "</form>\n" +
                "<br>\n" +
                "<form action=\"/addToBuyList/"+ id +"\" method=\"POST\">\n" +
                "    <label>Your ID:</label>\n" +
                "    <input type=\"text\" name=\"user_id\" value=\"\" />\n" +
                "    <br><br>\n" +
                "    <button type=\"submit\">Add to BuyList</button>\n" +
                "</form>\n" +
                "<br />";
        commodityHTML += readHTMLPage("Commodity_end.html");
        return commodityHTML;
    }
}
