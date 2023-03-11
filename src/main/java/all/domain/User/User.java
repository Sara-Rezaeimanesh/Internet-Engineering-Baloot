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
    private ArrayList<Product> purchaseList;


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
        this.purchaseList = new ArrayList<>();
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
        System.out.println("\"data\": {\"buyList\": " + ow.writeValueAsString(buyList) + "}");

    }

    public void removeProduct(Product p) { buyList.remove(p); }

    public String createHTMLForUser() {
        return "<li id=\"username\">Username:" + this.username + "</li>\n" +
                "<li id=\"email\">Email:" + this.email + "</li>\n" +
                "<li id=\"birthDate\">Birth Date:" + this.birthDate + "</li>\n" +
                "<li id=\"credit\">Credit:" + this.credit + "</li>\n" +
                "<li>\n" +
                "    <form action=\"\" method=\"POST\" >\n" +
                "        <label>Buy List Payment</label>\n" +
                "        <input id=\"form_credit\" type=\"number\" name=\"credit\" value=\"" + this.username +"\">\n" +
                "        <button type=\"submit\">Increase credit</button>\n" +
                "    </form>\n" +
                "</li>" +
                "<li>\n" +
                "    <form action=\"\" method=\"POST\" >\n" +
                "        <label>Buy List Payment</label>\n" +
                "        <input id=\"form_payment\" type=\"hidden\" name=\"userId\" value=\"" + this.username +"\">\n" +
                "        <button type=\"submit\">Payment</button>\n" +
                "    </form>\n" +
                "</li>";
    }

    public String createHTMLForBuyList() {
        String html = "";
        String removeString = "<td>        \n" +
                "                <form action=\"\" method=\"POST\" >\n" +
                "                    <input id=\"form_commodity_id\" type=\"hidden\" name=\"commodityId\" value= "+ username +">\n" +
                "                    <button type=\"submit\">Remove</button>\n" +
                "                </form>\n" +
                "            </td>";
        for(Product p : buyList)
            html += p.createHTML(removeString);
        return html;
    }

    public String createHTMLForPurchaseList() {
        String html = "";
        for(Product p : purchaseList)
            html += p.createHTML("");
        return html;
    }

    public void increaseCredit(int newCredit) {
        credit += newCredit;
    }
}