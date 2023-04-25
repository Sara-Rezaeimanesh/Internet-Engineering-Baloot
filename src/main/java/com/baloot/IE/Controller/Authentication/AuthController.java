package com.baloot.IE.Controller.Authentication;

import com.baloot.IE.domain.Amazon.Amazon;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam("username") String username,
                                @RequestParam("password") String password) throws IOException {
        Amazon amazon;
        try {
            amazon = Amazon.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(amazon.DoesUserExist(username, password)) {
            amazon.setActiveUser(username);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Login successful!");
            response.sendRedirect("http://localhost:8080/baloot");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid username or password!");
        }
    }

//    @PostMapping("/logout")
//    public ResponseEntity logout() {
//        try {
//            Bolbolestan.getInstance().makeLoggedOut();
//            return ResponseEntity.ok("ok");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("student not found. invalid login");
//        }
//    }
}

