package all.domain.Product;

import all.domain.Rating.Rating;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class Product {
    private int id;
    private String name;
    private int providerId;
    private int price;
    private ArrayList<String> categories;
    private int rating;
    private int inStock;
    @JsonIgnore
    private ArrayList<Rating> ratings;

    public boolean isSameCategory(ArrayList<String> categories_) {
        for(String c_ : categories_)
            for(String c : categories)
                return c.equals(c_);
        return false;
    }

    public Product(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("providerId") int providerId,
                   @JsonProperty("price") int price, @JsonProperty("categories") ArrayList<String> categories, @JsonProperty("rating") int rating,
                   @JsonProperty("inStock") int inStock) {
        this.id = id;
        this.name = name;
        this.providerId = providerId;
        this.price = price;
        this.categories = categories;
        this.rating = rating;
        this.inStock = inStock;
    }

    public void updateRating(Rating newRating) {
        int sumRating = ratings.size() * rating;
        boolean alreadyRated = false;
        for (Rating r : this.ratings)
            if ((newRating.getUsername()) == (r.getUsername())) {
                int diff = newRating.getRating() - r.getRating();
                sumRating = sumRating + diff;
                alreadyRated = true;
            }
        if (!alreadyRated) {
            sumRating = sumRating + newRating.getRating();
            ratings.add(newRating);
        }
        setRating(sumRating / ratings.size());
    }

    public boolean isInStock() {
        return inStock > 0;
    }

    public void updateStock(int i) {
        this.inStock += i;
    }
}
