package all.Controller.User;

import all.domain.Amazon.Amazon;
import all.domain.Product.Product;
import all.domain.User.User;

import java.util.ArrayList;

public class UserCotroller {
    private ArrayList<User> users;

    private static final String PRODUCT_HAS_BOUGHT_ERROR = "Product already bought!";
    private static final String PRODUCT_HAS_NOT_BOUGHT_ERROR = "Product hasn't already bought!";
    private static final String PRODUCT_IS_NOT_IN_STOCK = "Product is not in stock!";
    private static Amazon instance;
    private final String PRODUCT_ALREADY_EXIST_ERROR = "Product already exists!";
    private final String SUPPLIER_DOES_NOT_EXIST_ERROR = "Supplier does not exist!";
    private final String PRODUCT_DOES_NOT_EXIT_ERROR = "Product does not exist!";
    private final String USER_DOES_NOT_EXIST_ERROR = "User does not exist!";

    public boolean isAnybodyLoggedIn() {
        return activeUser != null;
    }
    public void setActiveUser(String userName) {
        activeUser = findUserById(userName);
    }

    public String getActiveUser() {
        if(activeUser == null)
            return "Not logged in";
        return activeUser.getUsername();
    }
    public void logout() {
        activeUser = null;
    }

    public boolean DoesUserExist(String username, String password){
        User user = findUserById(username);
        return user != null && user.isPassEqual(password);
    }

    public User findUserById(String uid) {
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

    public void increaseCredit(String username, int credit) throws Exception {
        if(credit <= 0) {
            errorMsg = "Credit must be more than zero";
            throw new Exception("Credit must be more than zero");
        }
        User u = findUserById(username);
        u.increaseCredit(credit);
    }

    public void addToBuyList() throws Exception {
        if(activeUser.hasBoughtProduct(chosenProduct.getId()))
        {
            errorMsg = PRODUCT_HAS_BOUGHT_ERROR;
            throw new Exception(PRODUCT_HAS_BOUGHT_ERROR);
        }
        if(!chosenProduct.isInStock())
        {
            errorMsg = PRODUCT_IS_NOT_IN_STOCK;
            throw new Exception(PRODUCT_IS_NOT_IN_STOCK);
        }
        activeUser.addProduct(chosenProduct);
        chosenProduct.updateStock(-1);
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






}
