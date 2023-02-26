package all.domain.Amazon;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import all.domain.Product.Product;
import all.domain.Supplier.Supplier;
import all.domain.User.User;

import java.util.ArrayList;

public class Amazon {
    private ArrayList<User> users;
    private ArrayList<Supplier> suppliers;
    private ArrayList<Product> products;

    public void addUser(User user){
        users.add(user);
    }

    public void addSupplier(Supplier supplier) { suppliers.add(supplier); }

    public void listCommodities() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String commodities = "";
        for(Product p : products)
            commodities += ow.writeValueAsString(p);

        System.out.println(commodities);
    }
}
