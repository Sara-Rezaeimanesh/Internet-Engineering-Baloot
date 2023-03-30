package all.Controller;

import java.io.*;
import java.util.Objects;

import all.domain.Amazon.Amazon;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "ProductFilterServlet", urlPatterns = "/filterProducts")
public class ProductFilterServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            Amazon amazon = Amazon.getInstance();
            String searchString = request.getParameter("search");
            amazon.saveSearchResults(searchString, action);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Commodities.jsp");
        requestDispatcher.forward(request, response);
    }
}