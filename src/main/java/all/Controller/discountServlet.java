package all.Controller;

import all.domain.Amazon.Amazon;
import all.domain.User.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "discountServlet", urlPatterns = "/discount")
public class discountServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Amazon amazon = null;
        try {
            amazon = Amazon.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String discountCode = request.getParameter("discount");
        try {
            amazon.applyDiscount(discountCode);
            response.sendRedirect("http://localhost:8080/baloot/buyList");
        } catch (Exception e) {
            response.sendRedirect("http://localhost:8080/baloot/error");
        }
    }
}