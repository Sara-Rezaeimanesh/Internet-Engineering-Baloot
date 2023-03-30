package all.domain.Amazon;

import all.domain.Comment.Comment;
import all.domain.Discount.Discount;
import all.domain.Rating.Rating;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import all.domain.Product.Product;
import all.domain.Supplier.Supplier;
import all.domain.User.User;
import com.google.common.io.Resources;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class Amazon {
    private static final String PRODUCT_HAS_BOUGHT_ERROR = "Product already bought!";
    private static final String PRODUCT_HAS_NOT_BOUGHT_ERROR = "Product hasn't already bought!";
    private static final String PRODUCT_IS_NOT_IN_STOCK = "Product is not in stock!";
    private static Amazon instance;
    private final String PRODUCT_ALREADY_EXIST_ERROR = "Product already exista!";
    private final String SUPPLIER_DOES_NOT_EXIST_ERROR = "Supplier is not exist!";
    private final String PRODUCT_DOES_NOT_EXIT_ERROR = "Product does not exist!";
    private final String USER_DOES_NOT_EXIST_ERROR = "User does not exist!";
    private String activeUser = null;

    private ArrayList<User> users = new ArrayList<>();

    private ArrayList<Supplier> suppliers = new ArrayList<>();

    private ArrayList<Product> products = new ArrayList<>();

    private ArrayList<Discount> discounts;

    private static String errorMsg;

    public static String getErrorMsg(){
        return errorMsg;
    }
    public static void setErrorMsg(String err){
         errorMsg = err;
    }
    public Amazon() throws Exception {
        Initializer initializer = new Initializer();
        suppliers = initializer.getProvidersFromAPI("providers");
        suppliers.forEach(Supplier::initialize);
        users = initializer.getUsersFromAPI("users");
        users.forEach(User::initialize);
        products = initializer.getCommoditiesFromAPI("commodities");
        products.forEach(Product::initialize);
        addToBuyList(users.get(0).getUsername(), products.get(0).getId());
        addToBuyList(users.get(0).getUsername(), products.get(1).getId());
        discounts = initializer.getDiscountsFromAPI("discount");
        discounts.forEach(Discount::initialize);
        for(Discount d : discounts)
            d.print();
        ArrayList<Comment> comments = initializer.getCommentsFromAPI("comments");
        comments.forEach(Comment::initialize);
        for(Comment c : comments){
            for(User user : users)
                if(Objects.equals(user.getEmail(), c.getUserEmail()))
                    c.updateUserId(user.getUsername());

            Product p = findProductsById(c.getCommodityId());
            assert p != null;
            p.addComment(c);
        }
    }

    public static Amazon getInstance() throws Exception {
        if(instance == null)
            instance = new Amazon();
        return instance;
    }

    private Supplier findSupplierById(int id) {
        for (Supplier s : suppliers)
            if (id == s.getId())
                return s;
        return null;
    }

    private Product findProductsById(int id) {
        for (Product p : products)
            if (id == p.getId())
                return p;
        return null;
    }

    public User findUserById(String uid) {
        for(User u : users)
            if(u.userNameEquals(uid))
                return u;
        return null;
    }

    public Discount findDiscountById(String did) {
        for(Discount d : discounts)
            if(d.discountCodeEquals(did))
                return d;
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
        Supplier s = findSupplierById(product.getProviderId());
        if (s == null)
            throw new Exception(SUPPLIER_DOES_NOT_EXIST_ERROR);
        if (findProductsById(product.getId()) != null)
            throw new Exception(PRODUCT_ALREADY_EXIST_ERROR);
        products.add(product);
        s.addProductToSupplierList(product.getId());
    }

    public void addSupplier(Supplier supplier) {
        suppliers.add(supplier);
    }

    public void increaseCredit(String username, int credit) {
        if(credit <= 0)
            errorMsg = "Credit must be more than zero";
        User u = findUserById(username);
        u.increaseCredit(credit);
    }

    public void listCommodities() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        System.out.println("\"data\": {\"commoditiesList\": " + ow.writeValueAsString(products) + "}");
    }

    public void rateCommodity(Rating rating) throws Exception {
        if (findUserById(rating.getUsername()) == null)
            throw new Exception(USER_DOES_NOT_EXIST_ERROR);
        Product p = findProductsById(rating.getProductId());
        if(p != null)
            p.updateRating(rating);
        else
            throw new Exception(PRODUCT_DOES_NOT_EXIT_ERROR);
    }

    public void printJsonProductById(Product p) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        System.out.println("\"data\": " + ow.writeValueAsString(p));
    }

    public Product getCommodityById(Integer id) throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Product p = findProductsById(id);
        if(p != null) return p;
        else throw new Exception(PRODUCT_DOES_NOT_EXIT_ERROR);
    }

    public void printJsonCategory(ArrayList<Product> sameCat) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        System.out.println("\"data\": {\"commoditiesListByCategory\": " + ow.writeValueAsString(sameCat) + "}");
    }

    public ArrayList<Product> getCommoditiesByCategory(String category){
        ArrayList<Product> sameCatProduct = new ArrayList<>();
        for (Product p : products) {
                if (p.isSameCategory(category))
                    sameCatProduct.add(p);
        }
        return sameCatProduct;
    }

    public void addToBuyList(String username, int commodityId) throws Exception {
        Product p = findProductsById(commodityId);
        if(p == null)
            throw new Exception(PRODUCT_DOES_NOT_EXIT_ERROR);
        User u = findUserById(username);
        if(u == null)
            throw new Exception(USER_DOES_NOT_EXIST_ERROR);
        if(u.hasBoughtProduct(commodityId))
            throw new Exception(PRODUCT_HAS_BOUGHT_ERROR);
        if(!p.isInStock())
            throw new Exception(PRODUCT_IS_NOT_IN_STOCK);
        u.addProduct(p);
        p.updateStock(-1);
    }

    public void removeFromBuyList(String username, int commodityId) throws Exception {
        Product p = findProductsById(commodityId);
        if(p == null)
            throw new Exception(PRODUCT_DOES_NOT_EXIT_ERROR);
        User u = findUserById(username);
        if(u == null)
            throw new Exception(USER_DOES_NOT_EXIST_ERROR);
        if(!u.hasBoughtProduct(commodityId))
            throw new Exception(PRODUCT_HAS_NOT_BOUGHT_ERROR);
        u.removeProduct(p);
        p.updateStock(1);
    }

    public void getUserBuyList(String name) throws Exception {
        User u = findUserById(name);
        if(u == null) throw new Exception(USER_DOES_NOT_EXIST_ERROR);
        u.printBuyList();
    }

    public String createCommoditiesPage(ArrayList<Product> products_, String flag) throws Exception {
        if(Objects.equals(flag, "all"))
            products_ = products;
        String commoditiesHTML = readHTMLPage("Commodities_start.html");
        for(Product p : products_)
            commoditiesHTML += p.createHTML("");
        commoditiesHTML += readHTMLPage("Commodities_end.html");

        return commoditiesHTML;
    }

    private String readHTMLPage(String fileName) throws Exception {
        File file = new File(Resources.getResource("templates/" + fileName).toURI());
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    private void addComment(Comment c) {
        for(User user : users)
            if(Objects.equals(user.getEmail(), c.getUserEmail()))
                c.updateUserId(user.getUsername());

        Product p = findProductsById(c.getCommodityId());
        assert p != null;
        p.addComment(c);
    }

    public String createCommodityPage(int id) throws Exception {
        String commodityHTML = readHTMLPage("Commodity_start.html");
        Product p = findProductsById(id);
        if(p == null)
            throw new Exception(PRODUCT_DOES_NOT_EXIT_ERROR);

        commodityHTML += p.createHTMLForCommodity();
        commodityHTML += readHTMLPage("Commodity_end.html");
        commodityHTML = commodityHTML.replaceAll("rateCommodity", "rateCommodity/"+id);
        commodityHTML = commodityHTML.replaceAll("addToBuyList", "addToBuyList/"+id);
        commodityHTML += createCommentHTML(id) + "</table>\n\n</body>\n</html>";

        return commodityHTML;
    }

    private String createCommentHTML(int id) {
        for(Product p : products)
            if(p.getId() == id)
                return p.createCommentsHTML();

        return "";
    }

    public String createProviderPage(String id) throws Exception {
        String providerHTML = readHTMLPage("Provider_start.html");
        Supplier p = findSupplierById(Integer.parseInt(id));
        if(p == null)
            return readHTMLPage("404.html");

        providerHTML += p.createHTMLForProvider();
        providerHTML += readHTMLPage("Provider_middle.html");
        ArrayList<Integer> productsId = p.getProducts();
        ArrayList<Product> products = new ArrayList<Product>();
        for(Integer i : productsId){
            Product product = findProductsById(i);
            assert product != null;
            providerHTML += product.createHTML("");
        }
        providerHTML += readHTMLPage("Provider_end.html");
        return providerHTML;
    }


    public String createUserPage(String id) throws Exception {
        String providerHTML = readHTMLPage("User_start.html");
        User u = findUserById(id);
        if(u == null)
            return readHTMLPage("404.html");

        providerHTML += u.createHTMLForUser();
        providerHTML += readHTMLPage("User_middle.html");
        String removeAction = "\"/removeFromBuyList/";
        providerHTML += u.createHTMLForBuyList(removeAction);
        providerHTML += readHTMLPage("User_middle2.html");
        providerHTML += u.createHTMLForPurchaseList();
        providerHTML += readHTMLPage("User_end.html");
        return providerHTML;
    }

    public void voteComment(String commentId, int vote) throws Exception {
        Product p = findCommentCommodity(commentId);
        if(p == null)
            throw new Exception(PRODUCT_DOES_NOT_EXIT_ERROR);
        p.voteComment(commentId, vote);
    }

    public ArrayList<Product> getCommodityByPriceInterval(int startPrice, int endPrice) {
        ArrayList<Product> samePriceProducts = new ArrayList<>();
        for (Product p : products) {
            if (p.isInPriceInterval(startPrice, endPrice)) {
                System.out.println("in interval" + p.getId());
                samePriceProducts.add(p);
            }
        }
        return samePriceProducts;
    }

    public Product findCommentCommodity(String commentId) {
        for(Product p : products)
            if(p.hasComment(Integer.parseInt(commentId)))
                return p;
        return null;
    }

    public boolean isAnybodyLoggedIn() {
        return activeUser != null;
    }

    public String getActiveUser() {
        if(activeUser == null)
            return "Not logged in";
        return activeUser;
    }

    public boolean DoesUserExist(String username, String password){
        User user = findUserById(username);
        return user != null && user.isPassEqual(password);
    }

    public void setActiveUser(String userName){
        activeUser = userName;
    }
    public void logout() {
        activeUser = null;
    }

    public void applyDiscount(String discountCode) throws Exception {
        User user = findUserById(activeUser);
        Discount discount = findDiscountById(discountCode);
        if(discount == null) {
            errorMsg = "This discount does not exist";
            throw new Exception();
        }
        if(discount.isValidToUse(user.getUsername())) {
            user.applyDiscount(discount.getDiscount());
            discount.addToUsed(user.getUsername());
        }
        else{
            errorMsg = "You have already use this discount code";
            throw new Exception();
        }

    }


}
