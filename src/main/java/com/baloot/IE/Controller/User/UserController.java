package com.baloot.IE.Controller.User;

import com.baloot.IE.domain.User.User;
import com.baloot.IE.domain.User.UserRepository;
import com.baloot.IE.domain.User.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserRepository userRepository;

    @Autowired
    public UserController() throws Exception {
        userRepository = UserRepository.getInstance();
    }

    @GetMapping("")
    public List<User> all() {
        return userRepository.getAllUsers();
    }

    @GetMapping("/{id}")
    public User one(@PathVariable String id) {
        return userRepository.findUserById(id);
    }
}
