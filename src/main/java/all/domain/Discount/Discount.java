package all.domain.Discount;

import java.util.ArrayList;
import java.util.Objects;

public class Discount {
    private String discountCode;
    private String discount;

    private ArrayList<String> usedByUsers;

    public Discount(String discountCode, String discount) {
        this.discountCode = discountCode;
        this.discount = discount;
    }

    public void initialize() {
        usedByUsers = new ArrayList<>();
    }

    public boolean discountCodeEquals(String did) {
        return Objects.equals(did, discountCode);
    }

    public String getDiscount() {
        return discount;
    }

    public void print() {
        System.out.println(discountCode + " " + discount);
    }

    public boolean isValidToUse(String username) {
        return !usedByUsers.contains(username);
    }

    public void addToUsed(String username) {
        usedByUsers.add(username);
    }
}
