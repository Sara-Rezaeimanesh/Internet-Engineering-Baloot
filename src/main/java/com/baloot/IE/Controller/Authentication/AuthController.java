package com.baloot.IE.Controller.Authentication;
import com.baloot.IE.JWT.utility.JwtUtils;
import com.baloot.IE.domain.User.User;
import com.baloot.IE.domain.User.UserManager;
import com.baloot.IE.domain.User.UserView;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("")
public class AuthController {
    @PostMapping("/login")
    public User login(HttpServletResponse response,
                        @RequestBody Map<String, String> body) throws Exception {
        UserManager userRepository = UserManager.getInstance();
        if(userRepository.userExists(body.get("username"), body.get("password"))) {
            String token = JwtUtils.createJWT(body.get("username"));
            User user = userRepository.findUserById(body.get("username"));
            user.setToken(token);
            return user;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new Exception("Invalid username or password!");
        }
    }

    @PostMapping("/signup")
    public User signUp(HttpServletResponse response,
                       @RequestBody UserView userView) throws Exception {
        UserManager userRepository = UserManager.getInstance();
        User newUser = userView.viewToUser();
        userRepository.addUser(newUser);
        String token = JwtUtils.createJWT(userView.getUsername());
        newUser.setToken(token);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return newUser;
    }
}

