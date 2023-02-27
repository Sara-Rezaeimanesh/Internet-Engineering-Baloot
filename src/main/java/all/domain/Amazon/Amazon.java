package all.domain.Amazon;
import all.domain.Rating.Rating;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import all.domain.Product.Product;
import all.domain.Supplier.Supplier;
import all.domain.User.User;

import java.util.ArrayList;
import java.util.Objects;

public class Amazon {
    private ArrayList<User> users = new ArrayList<>();;
    private ArrayList<Supplier> suppliers = new ArrayList<>();;
    private ArrayList<Product> products = new ArrayList<>();;

    public Amazon() {
    }

    public boolean isInSuppliers(int id){
        for(Supplier s : suppliers)
            if (id == s.getId())
                return true;
        return false;
    }

    public boolean isInProducts(int id){
        for(Product p : products)
            if (id == p.getId())
                return true;
        return false;
    }

    public boolean isInUsers(int id){
        for(User u : users)
            if (id == u.getId())
                return true;
        return false;
    }

    public void addUser(User user){
        for(User user_ : this.users)
            if(Objects.equals(user_.getId(), user.getId())){
                user_.setUsername(user.getUsername());
                user_.setAddress(user.getAddress());
                user_.setBirthDate(user.getBirthDate());
                user_.setCredit(user.getCredit());
                user_.setEmail(user.getEmail());
                user_.setPassword(user.getPassword());
            }
        users.add(user);
    }

    public void addProduct(Product product) throws Exception {
        if(!isInSuppliers(product.getProviderId()))
            throw new Exception("Supplier is not exist\n");
        if(isInProducts(product.getId()))
            throw new Exception("Product already exist\n");
        products.add(product);
    }

    public void addSupplier(Supplier supplier) { suppliers.add(supplier); }

    public void listCommodities() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String commodities = "";
        for(Product p : products)
            commodities += ow.writeValueAsString(p);

        System.out.println(commodities);
    }

    public void rateCommodity(Rating rating) throws Exception {
        if(!isInUsers(rating.getUsername()))
            throw new Exception("User is not exist\n");
        if(!isInProducts(rating.getProductId()))
            throw new Exception("Product is not exist\n");


    }
}
