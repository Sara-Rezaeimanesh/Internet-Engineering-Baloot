package all.Controller;

import java.io.*;
import java.util.Arrays;

import all.domain.Amazon.Amazon;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "CommoditiesServlet", urlPatterns = "/commodities")
public class CommoditiesServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Amazon amazon = Amazon.getInstance();
            if(amazon.isAnybodyLoggedIn()) {
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("Commodities.jsp");
                requestDispatcher.forward(request, response);
            }
            else
                response.sendRedirect("http://localhost:8080/baloot/Login");

        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}