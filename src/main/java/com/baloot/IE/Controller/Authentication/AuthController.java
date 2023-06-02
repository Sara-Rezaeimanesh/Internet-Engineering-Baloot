package com.baloot.IE.Controller.Authentication;
import com.baloot.IE.JWT.utility.JwtUtils;
import com.baloot.IE.domain.User.User;
import com.baloot.IE.domain.User.UserManager;
import com.baloot.IE.domain.User.UserView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.OffsetDateTime;
import java.util.Map;

@RestController
@RequestMapping("")
public class AuthController {
    @PostMapping("/login")
    public User login(HttpServletResponse response,
                        @RequestBody Map<String, String> body) throws Exception {
        UserManager userManager = UserManager.getInstance();
        if(userManager.userExists(body.get("username"), body.get("password"))) {
            String token = JwtUtils.createJWT(body.get("username"));
            System.out.println(token);
            User user = userManager.findUserById(body.get("username"));
            user.setToken(token);
            return user;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new Exception("Invalid username or password!");
        }
    }

    public static String postRequest(String url) throws Exception{
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();

            String result = "";
            if (entity != null)
                result = EntityUtils.toString(entity);
            return result;
        }
    }

    public static String getRequestForAuthorization(String url, String code) throws Exception{
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        request.setHeader("Authorization", "Bearer "+code);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();

            String result = "";
            if (entity != null)
                result = EntityUtils.toString(entity);
            return result;
        }
    }


    @PostMapping("/signup")
    public User signUp(HttpServletResponse response,
                       @RequestBody UserView userView) throws Exception {
        UserManager userManager = UserManager.getInstance();
        User newUser = userView.viewToUser();
        userManager.addUser(newUser);
        String token = JwtUtils.createJWT(userView.getUsername());
        newUser.setToken(token);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return newUser;
    }

    @PostMapping("/signup/github")
    public User signUp(HttpServletResponse response,
                       @RequestParam String code) throws Exception {
        String res = postRequest("https://github.com/login/oauth/access_token?client_id=98bc63aeaf3e8a4f802c&client_secret=a73eeb3f06113a45f16e84369b323b56112dd621&code="+code);
        String[] args = res.split("&");
        String[] token_args = args[0].split("=");
        String token = token_args[1];

        res = getRequestForAuthorization("https://api.github.com/user", token);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> jsonMap = objectMapper.readValue(res, Map.class);

        UserManager userManager = UserManager.getInstance();
        OffsetDateTime dateTime = OffsetDateTime.parse(jsonMap.get("created_at"));
        OffsetDateTime newDateTime = dateTime.minusYears(18);

        User newUser;
        try{
            newUser = userManager.findUserByEmail(jsonMap.get("email"));
        } catch(Exception e) {
            newUser = new User(jsonMap.get("login"), null, jsonMap.get("email"), String.valueOf(newDateTime), null, 0);
        }
        userManager.addGithubUser(newUser);
        String JWTToken = JwtUtils.createJWT(jsonMap.get("login"));
        newUser.setToken(JWTToken);
        response.setStatus(HttpServletResponse.SC_CREATED);

        System.out.println(JWTToken);

        return newUser;
    }

}

