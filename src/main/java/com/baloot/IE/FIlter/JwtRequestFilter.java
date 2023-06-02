package com.baloot.IE.FIlter;

import com.baloot.IE.JWT.utility.JwtUtils;
import com.baloot.IE.domain.User.User;
import com.baloot.IE.domain.User.UserManager;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@Component
public class JwtRequestFilter implements Filter {

    UserManager userManager = UserManager.getInstance();

    public JwtRequestFilter() throws Exception {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String url = request.getRequestURI();
        if(url.equals("/login") || url.equals("/signup"))
            chain.doFilter(request, response);
        else {
            String token = request.getHeader("Authorization");
            if(token == null)
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            else {
                String username = JwtUtils.verifyJWT(token);
                if(username == null) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
                else {
                    User user = userManager.findUserById(username);
                    request.setAttribute("user", user.getUsername());
                    chain.doFilter(request, response);
                }
            }
        }
    }
}
