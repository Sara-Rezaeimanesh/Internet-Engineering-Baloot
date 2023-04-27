package com.baloot.IE.domain.Amazon;

import com.baloot.IE.domain.Discount.Discount;
import com.baloot.IE.domain.Product.ProductRepository;
import com.baloot.IE.domain.Product.Product;
import com.baloot.IE.domain.Supplier.Supplier;
import com.baloot.IE.domain.User.User;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Amazon {
    private static final String PRODUCT_HAS_BOUGHT_ERROR = "Product already bought!";
    private static final String PRODUCT_HAS_NOT_BOUGHT_ERROR = "Product hasn't already bought!";
    private static final String PRODUCT_IS_NOT_IN_STOCK = "Product is not in stock!";
    private static Amazon instance;
    private final String PRODUCT_ALREADY_EXIST_ERROR = "Product already exists!";
    private final String SUPPLIER_DOES_NOT_EXIST_ERROR = "Supplier does not exist!";
    private final String PRODUCT_DOES_NOT_EXIT_ERROR = "Product does not exist!";
    private final String USER_DOES_NOT_EXIST_ERROR = "User does not exist!";
    ArrayList<Product> suggestedProduct;


    public static HashMap<Product, Float>
    sortByValue(HashMap<Product, Float> hm)
    {
        HashMap<Product, Float> temp
                = hm.entrySet()
                .stream()
                .sorted((i1, i2)
                        -> i2.getValue().compareTo(
                        i1.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
        return temp;
    }

//    public void setSuggestedProduct(){
//        suggestedProduct = new ArrayList<>();
//        assert chosenProduct != null;
//        int id = chosenProduct.getId();
//        HashMap<Product, Float> ratedProduct = new HashMap<>();
//        for(Product p : products){
//            float score = 0;
//            if(id != p.getId())
//                for(String category : p.getCategories())
//                    if(chosenProduct.isSameCategory(category))
//                        score += 11;
//            score += p.getRating();
//            ratedProduct.put(p, score);
//        }
//        HashMap<Product, Float> sortedProduct = sortByValue(ratedProduct);
//        int i = 0;
//        for (Map.Entry<Product, Float> set : sortedProduct.entrySet()) {
//            suggestedProduct.add(set.getKey());
//            i ++;
//            if(i == 5)
//                break;
//        }
//    }

    private ArrayList<User> users;

    private ArrayList<Supplier> suppliers;

    private ProductRepository productRepository;

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
        productRepository = ProductRepository.getInstance();
        suppliers = initializer.getProvidersFromAPI("providers");
        suppliers.forEach(Supplier::initialize);
        users = initializer.getUsersFromAPI("users");
        users.forEach(User::initialize);
//        discounts = initializer.getDiscountsFromAPI("discount");
//        discounts.forEach(Discount::initialize);
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

    public void addSupplier(Supplier supplier) {
        suppliers.add(supplier);
    }

    public void increaseCredit(String username, int credit) throws Exception {
        if(credit <= 0) {
            errorMsg = "Credit must be more than zero";
            throw new Exception("Credit must be more than zero");
        }
        User u = findUserById(username);
        u.increaseCredit(credit);
    }

//    public void addToBuyList() throws Exception {
//        if(activeUser.hasBoughtProduct(chosenProduct.getId()))
//        {
//            errorMsg = PRODUCT_HAS_BOUGHT_ERROR;
//            throw new Exception(PRODUCT_HAS_BOUGHT_ERROR);
//        }
//        if(!chosenProduct.isInStock())
//        {
//            errorMsg = PRODUCT_IS_NOT_IN_STOCK;
//            throw new Exception(PRODUCT_IS_NOT_IN_STOCK);
//        }
//        activeUser.addProduct(chosenProduct);
//        chosenProduct.updateStock(-1);
//    }

    public void removeFromBuyList(String username, int commodityId) throws Exception {
        Product p = productRepository.findProductsById(commodityId);
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

    public boolean DoesUserExist(String username, String password){
        User user = findUserById(username);
        return user != null && user.isPassEqual(password);
    }

//    public void applyDiscount(String discountCode) throws Exception {
//        Discount discount = findDiscountById(discountCode);
//        if(discount == null) {
//            errorMsg = "This discount does not exist";
//            throw new Exception();
//        }
//        if(discount.isValidToUse(activeUser.getUsername())) {
//            activeUser.applyDiscount(discount.getDiscount());
//            discount.addToUsed(activeUser.getUsername());
//        }
//        else{
//            errorMsg = "You have already use this discount code";
//            throw new Exception();
//        }
//
//    }
}
