package all.domain.Product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Product {
    private int id;
    private String name;
    private int providerId;
    private int price;
    private String category;
    private int rating;
    private int inStock;

    public Product(int id, String name, int providerId, int price, String category, int rating, int inStock) {
        this.id = id;
        this.name = name;
        this.providerId = providerId;
        this.price = price;
        this.category = category;
        this.rating = rating;
        this.inStock = inStock;
    }

}
