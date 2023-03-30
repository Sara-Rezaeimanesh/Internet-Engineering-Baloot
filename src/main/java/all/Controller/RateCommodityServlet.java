package all.Controller;

import java.io.*;

import all.domain.Amazon.Amazon;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "RateCommodityServlet", urlPatterns = "/rateCommodity")
public class RateCommodityServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Amazon amazon = Amazon.getInstance();
            amazon.addRating(request.getParameter("quantity"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("Commodity.jsp");
        requestDispatcher.forward(request, response);
    }
}