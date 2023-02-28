package all.domain.Rating;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rating {
    private String username;
    private int productId;
    private float score;

    public Rating(@JsonProperty("username") String username, @JsonProperty("commodityId") int productId, @JsonProperty("score") float rating) throws Exception {
        this.username = username;
        this.productId = productId;
        if (rating > 10 || rating < 1) throw new Exception("Rating must be between 1 to 10\n");
        this.score = rating;
    }
}
