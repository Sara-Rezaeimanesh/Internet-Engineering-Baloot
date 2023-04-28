package com.baloot.IE.Controller.Actions;

import com.baloot.IE.domain.User.User;
import com.baloot.IE.domain.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("")
public class UserActionsController {
    private UserRepository userRepository;

    @Autowired
    public UserActionsController() throws Exception {
        userRepository = UserRepository.getInstance();
    }
}
