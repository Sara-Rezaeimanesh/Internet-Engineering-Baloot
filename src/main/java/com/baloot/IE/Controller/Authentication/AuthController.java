package com.baloot.IE.Controller.Authentication;

import com.baloot.IE.domain.Amazon.Amazon;
import com.baloot.IE.domain.Product.ProductRepository;
import com.baloot.IE.domain.Session.Session;
import com.baloot.IE.domain.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final Session session;

    @Autowired
    public AuthController() {
        session = Session.getInstance();
    }

    @PostMapping("/login")
    public User login(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam("username") String username,
                                @RequestParam("password") String password) throws Exception {
        Amazon amazon = Amazon.getInstance();
        if(amazon.DoesUserExist(username, password)) {
            User user = amazon.findUserById(username);
            session.setActiveUser(user);
            return user;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new Exception("Invalid username or password!");
        }
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        session.logout();
    }
}

