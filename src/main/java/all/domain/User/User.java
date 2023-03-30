package all.domain.User;
import all.domain.Product.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    private int discount;
    private ArrayList<Product> buyList;
    private ArrayList<Product> purchaseList;

    public void initialize() {
        buyList = new ArrayList<>();
        purchaseList = new ArrayList<>();
    }

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

    private int calculateCurrBuyListPrice(){
        int price = 0;
        for(Product p : buyList)
            price += p.getPrice();
        return (price*(100-discount))/100;
    }

    public String createHTMLForUser() {
        return "<li id=\"username\">Username:" + this.username + "</li>\n" +
                "<li id=\"email\">Email:" + this.email + "</li>\n" +
                "<li id=\"birthDate\">Birth Date:" + this.birthDate + "</li>\n" +
                "<li id=\"credit\">Credit:" + this.credit + "</li>\n" +
                "<li>Current Buy List Price: "+ this.calculateCurrBuyListPrice() +"</li>" +
                "<li> <a href=\"credit\">Add Credit</a> </li>" +
                "<li>" +
                "<form action=\"payment\" method=\"POST\">" +
                    "<label>Submit & Pay</label>" +
                    "<input id=\"form_payment\" type=\"hidden\" name=\"userId\" value=\" " + this.username +"\">" +
                    "<button type=\"submit\">Payment</button>" +
                "</form>" +
                "</li>";
    }

    public String createHTMLForBuyList(String removeAction) {
        String html = "";
        for(Product p : buyList) {
            if(Objects.equals(removeAction, "/removeFromBuyList/"))
                removeAction += username + "/" + p.getId() + " ";
            String removeString = "<td>        \n" +
                    "                <form action=" + removeAction + " method=\"POST\" >\n" +
                    "                    <input id=\"form_commodity_id\" type=\"hidden\" name=\"commodityId\" value= " + p.getId() + ">\n" +
                    "                    <button type=\"submit\">Remove</button>\n" +
                    "                </form>\n" +
                    "            </td>";
            html += p.createHTML(removeString);
        }
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

    public String getEmail() {
        return email;
    }

    public boolean isPassEqual(String password) {
        return Objects.equals(this.password, password);
    }

    public String getPassword() {
        return password;
    }

    public void payBuyList() throws Exception {
        if(credit < calculateCurrBuyListPrice())
            throw new Exception("Credit is not enough");
        credit -= calculateCurrBuyListPrice();
        purchaseList.addAll(buyList);
        buyList.clear();
    }

    public void applyDiscount(String discount) {
        this.discount = Integer.parseInt(discount);
    }
}