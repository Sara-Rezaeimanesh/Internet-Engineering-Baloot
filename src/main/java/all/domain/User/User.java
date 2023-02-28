package all.domain.User;
import all.domain.Product.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;


@NoArgsConstructor
public class User {
    private String username;
    private String password;
    private String email;
    private String birthDate;
    private String address;
    private int credit;
    private ArrayList<Product> buyList;

    public void updateUserInfo(User newUserInfo){
        username = newUserInfo.username;
        password = newUserInfo.password;
        address = newUserInfo.address;
        birthDate = newUserInfo.birthDate;
        credit = newUserInfo.credit;
        email = newUserInfo.email;
    }

    public String getUsername() {
        return username;
    }

    public User(@JsonProperty("username") String username_,
                @JsonProperty("password") String password_,
                @JsonProperty("email") String email_,
                @JsonProperty("birthDate") String birthDate_,
                @JsonProperty("address") String address_,
                @JsonProperty("credit")  int credit_) throws Exception {
        if(!Pattern.matches("^[._a-zA-Z0-9]+$", username_))
            throw new Exception("Username cannot contain especial characters.\n");
        this.username = username_;
        this.password = password_;
        this.email = email_;
        this.address = address_;
        this.credit = credit_;
        this.buyList = new ArrayList<>();
    }

    public boolean hasBoughtProduct(int commodityId) {
        for(Product p : buyList)
            if(p.getId() == commodityId)
                return true;
        return false;
    }

    public void addProduct(Product p) { buyList.add(p); }

    public boolean userNameEquals(String username) {
        return Objects.equals(this.username, username);
    }

    public void printBuyList() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        StringBuilder commodities = new StringBuilder();
        for (Product p : buyList)
            commodities.append(ow.writeValueAsString(p));

        System.out.println("\"data\": " + commodities);

    }
}