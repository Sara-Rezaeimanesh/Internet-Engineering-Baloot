package com.baloot.IE.Controller.Authentication;

import com.baloot.IE.domain.Amazon.Amazon;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam("username") String username,
                                @RequestParam("password") String password) throws Exception {
        Amazon amazon = Amazon.getInstance();
        if(amazon.DoesUserExist(username, password)) {
            amazon.setActiveUser(username);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new Exception("Invalid username or password!");
        }
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Amazon amazon = Amazon.getInstance();
        amazon.logout();
    }
}

