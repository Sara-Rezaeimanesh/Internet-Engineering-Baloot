package all.domain.Product;

import all.domain.Comment.Comment;
import all.domain.Rating.Rating;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Objects;

@Getter
@Setter
@ToString
public class Product {
    private int id;
    private String name;
    private int providerId;
    private int price;
    private ArrayList<String> categories;
    private int inStock;
    private float rating;
    @JsonIgnore
    private ArrayList<Rating> ratings;
    @JsonIgnore
    private ArrayList<Comment> comments;

    public boolean isSameCategory(String category) {
        boolean isEqualCategory = false;
        for (String c : categories)
            if (c.equals(category)) {
                return true;
            }
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
        this.ratings = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public void initialize() {
        ratings = new ArrayList<>();
        comments = new ArrayList<>();
    }

    public void addComment(Comment comment){
        comments.add(comment);
    }

    public void updateRating(Rating newRating) {
        System.out.println(newRating.getScore());
        float sumRating = ratings.size() * rating;
        boolean alreadyRated = false;
        for (Rating r : this.ratings)
            if (Objects.equals(newRating.getUsername(), r.getUsername())) {
                float diff = newRating.getScore() - r.getScore();
                r.setScore(newRating.getScore());
                sumRating = sumRating + diff;
                alreadyRated = true;
            }
        if (!alreadyRated) {
            sumRating = sumRating + newRating.getScore();
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

    private String createCatString() {
        String catString = "";
        for(int i = 0; i < categories.size(); i++)
            catString += i + 1 == categories.size() ? categories.get(i) : categories.get(i) + ", ";
        return catString;
    }

    public String createHTML(String removeButton) {
        String tds = "\t<td>";
        String tde = "</td>";
        String trs = "<tr>";
        String tre = "</tr>";

        return trs + "\n" + tds + id + tde + "\n" + tds + name + tde + "\n" +
                tds + providerId + tde + "\n" + tds + price + tde +
                tds + createCatString() + tde + "\n" + tds + rating + tde +
                tds + inStock + tde + "\n" +
                tds + "<a href=\"/commodities/" + id + "\">Link</a>" + tde + removeButton +tre;
    }

    public String createHTMLForCommodity() {
        return "<ul>" + "<li id=\"id\">Id: " + id + "</li>" + "\n" +
               "<li id=\"name\">Name:" + name + "</li>" + "\n" +
               "<li id=\"providerId\">Provider Id:" + providerId + "</li>" + "\n" +
               "<li id=\"price\">Price: " + price + "</li>" + "\n" +
               "<li id=\"categories\">Categories: " +  createCatString() + "</li>" + "\n" +
               "<li id=\"rating\">Rating: " + rating  + "</li>" + "\n" +
               "<li id=\"inStock\">In Stock: " + inStock + "</li>" + "\n" + "</ul>";
    }

    public boolean isInPriceInterval(int startPrice, int endPrice) {
        System.out.println("prices" + startPrice + " " + price + " " + endPrice);
        return price <= endPrice && price >= startPrice;
    }

    public void voteComment(String commentId, int vote) {
        for(Comment c : comments)
            if(c.idMatches(Integer.parseInt(commentId)))
                c.updateVote(vote);
    }

    public boolean hasComment(int commentId) {
        for(Comment c : comments)
            if(c.idMatches(commentId))
                return true;

        return false;
    }

    public String createCommentsHTML() {
        String commentsHTML = "";
        for(Comment c : comments)
            commentsHTML += c.createHTML();
        return commentsHTML;
    }
}
