package all.Controller;

import java.io.*;

import all.domain.Amazon.Amazon;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "AddToBuyListServlet", urlPatterns = "/addToBuyList")
public class AddToBuyListServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Amazon amazon = Amazon.getInstance();
            amazon.addToBuyList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("Commodity.jsp");
        requestDispatcher.forward(request, response);
    }
}