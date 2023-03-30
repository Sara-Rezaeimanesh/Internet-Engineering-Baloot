package all.Controller;

import all.domain.Amazon.Amazon;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Arrays;

@WebServlet(name = "HomeServlet", urlPatterns = "")
public class HomeServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Amazon amazon = null;
        try {
            amazon = Amazon.getInstance();
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        assert amazon != null;
        if (amazon.isAnybodyLoggedIn()) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
            requestDispatcher.forward(request, response);
        } else {
            response.sendRedirect("http://localhost:8080/baloot/Login");
        }
    }
}
