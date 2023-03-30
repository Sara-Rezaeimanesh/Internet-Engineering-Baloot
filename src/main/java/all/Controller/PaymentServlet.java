package all.Controller;

import all.domain.Amazon.Amazon;
import all.domain.User.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(name = "PaymentServlet", urlPatterns = "/payment")
public class PaymentServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Amazon amazon = null;
        try {
            amazon = Amazon.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        User user = amazon.findUserById(amazon.getActiveUser());
        try {
            user.payBuyList();
            response.sendRedirect("http://localhost:8080/baloot/buyList");
        } catch (Exception e) {
            Amazon.setErrorMsg(e.getMessage());
            response.sendRedirect("http://localhost:8080/baloot/error");
        }
    }
}
