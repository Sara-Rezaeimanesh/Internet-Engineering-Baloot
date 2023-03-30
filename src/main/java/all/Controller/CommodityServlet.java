package all.Controller;

import java.io.*;
import java.util.Arrays;

import all.domain.Amazon.Amazon;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "CommodityServlet", urlPatterns = "/commodities/*")
public class CommodityServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        try {
            Amazon amazon = Amazon.getInstance();
            amazon.saveChosenProduct(Integer.parseInt(pathParts[1]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Commodity.jsp");
        requestDispatcher.forward(request, response);
    }
}