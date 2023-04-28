package com.baloot.IE.Controller.Authentication;

import com.baloot.IE.domain.Amazon.Amazon;
import com.baloot.IE.domain.Product.ProductRepository;
import com.baloot.IE.domain.Session.Session;
import com.baloot.IE.domain.User.User;
import com.baloot.IE.domain.User.UserRepository;
import com.baloot.IE.domain.User.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("")
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
        UserRepository userRepository = UserRepository.getInstance();
        if(userRepository.DoesUserExist(username, password)) {
            User user = userRepository.findUserById(username);
            session.setActiveUser(user);
            return user;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new Exception("Invalid username or password!");
        }
    }

    @GetMapping("/logout")
    public void logout() {
        session.logout();
    }

    @PostMapping("/signup")
    public User signUp(HttpServletRequest request, HttpServletResponse response,
                           @RequestBody UserView userView) throws Exception {
        UserRepository userRepository = UserRepository.getInstance();
        User newUser = userView.viewToUser();
        userRepository.addUser(newUser);
        session.setActiveUser(newUser);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return newUser;
    }
}

