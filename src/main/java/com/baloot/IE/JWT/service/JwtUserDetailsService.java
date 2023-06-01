package com.baloot.IE.JWT.service;

import java.util.ArrayList;

import com.baloot.IE.domain.User.User;
import com.baloot.IE.domain.User.UserManager;
import com.baloot.IE.repository.User.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    UserManager userManager = UserManager.getInstance();

    public JwtUserDetailsService() throws Exception {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userManager.findUserById(username);
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(),new ArrayList<>());
    }
}