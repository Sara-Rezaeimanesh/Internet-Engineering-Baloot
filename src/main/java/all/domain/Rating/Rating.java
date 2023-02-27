package all.domain.Rating;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rating {
    private int username;
    private int productId;
    private int rating;

    public Rating(int username, int productId, int rating) throws Exception {
        this.username = username;
        this.productId = productId;
        if(rating > 10 || rating < 1)
            throw new Exception("Rating must be between 1 to 10\n");
        this.rating = rating;
    }
}
