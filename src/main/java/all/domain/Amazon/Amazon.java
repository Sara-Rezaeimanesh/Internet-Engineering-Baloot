package all.domain.Amazon;

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
import java.util.Objects;

public class Amazon {
    private ArrayList<User> users = new ArrayList<>();
    ;
    private ArrayList<Supplier> suppliers = new ArrayList<>();
    ;
    private ArrayList<Product> products = new ArrayList<>();
    ;

    public Amazon() {
    }

    public boolean isInSuppliers(int id) {
        for (Supplier s : suppliers)
            if (id == s.getId())
                return true;
        return false;
    }

    public Product findProductsById(int id) {
        for (Product p : products)
            if (id == p.getId())
                return p;
        return null;
    }

    public boolean isInUsers(int id) {
        for (User u : users)
            if (id == u.getId())
                return true;
        return false;
    }

    public void addUser(User user) {
        for (User user_ : this.users)
            if (Objects.equals(user_.getId(), user.getId())) {
                user_.updateUserInfo(user);
            }
        users.add(user);
    }

    public void addProduct(Product product) throws Exception {
        if (!isInSuppliers(product.getProviderId()))
            throw new Exception("Supplier is not exist\n");
        if (findProductsById(product.getId()) != null)
            throw new Exception("Product already exist\n");
        products.add(product);
    }

    public void addSupplier(Supplier supplier) {
        suppliers.add(supplier);
    }

    public void listCommodities() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        StringBuilder commodities = new StringBuilder();
        for (Product p : products)
            commodities.append(ow.writeValueAsString(p));

        System.out.println(commodities);
    }

    public void rateCommodity(Rating rating) throws Exception {
        if (!isInUsers(rating.getUsername()))
            throw new Exception("User is not exist\n");
        Product p = findProductsById(rating.getProductId());
        if(p != null) p.updateRating(rating);
        else throw new Exception("Product does not exist!\n");
    }

    public void getCommodityById(Integer id) throws Exception {
        Product p = findProductsById(id);
        if(p != null) System.out.println(p.toString());
        else throw new Exception("Product does not exist!\n");
    }

    public void getCommoditiesByCategory(String category) {
        ArrayList<Product> tempProducts = new ArrayList<>();
        for (Product p : products) {
            if (p.isSameCategory(category))
                tempProducts.add(p);
        }
        System.out.println(tempProducts.toString());
    }

    private User findUserById(String uid) {
        for(User u : users)
            if(u.userNameEquals(uid))
                return u;
        return null;
    }

    public void addToBuyList(String username, int commodityId) throws Exception {
        Product p = findProductsById(commodityId);
        User u = findUserById(username);
        if(p == null) throw new Exception("Product does not exist!\n");
        if(u == null) throw new Exception("User does not exist!\n");
        if(u.hasBoughtProduct(commodityId))
            throw  new Exception("Product already added.");
        if(!p.isInStock()) throw new Exception("Product is not in stock!\n");

        u.addProduct(p);
    }
}
