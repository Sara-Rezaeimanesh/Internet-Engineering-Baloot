package all.Controller;
import java.io.*;

import all.domain.Amazon.Amazon;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Amazon amazon = null;
        try {
            amazon = Amazon.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        amazon.logout();
        response.sendRedirect("http://localhost:8080/baloot/Login");
    }
}