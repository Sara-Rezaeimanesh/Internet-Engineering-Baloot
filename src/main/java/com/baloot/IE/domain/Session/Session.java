package com.baloot.IE.domain.Session;

import com.baloot.IE.domain.Product.Product;
import com.baloot.IE.domain.User.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Session {
    private User activeUser;
    private Product chosenProduct;
    private static Session instance;
    public static Session getInstance() {
        if(instance == null)
            instance = new Session();
        return instance;
    }
    public Boolean isAnybodyLoggedIn() {
        return activeUser != null;
    }

    public void logout() {
        activeUser = null;
    }
}
