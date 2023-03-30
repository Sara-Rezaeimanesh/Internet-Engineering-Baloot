package all.Controller;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

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
            if(amazon.isAnybodyLoggedIn())
            {
                amazon.saveChosenProduct(Integer.parseInt(pathParts[1]));
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Commodity.jsp");
                requestDispatcher.forward(request, response);
            }
            else
                response.sendRedirect("http://localhost:8080/baloot/Login");
        } catch (Exception e) {
            response.sendRedirect("http://localhost:8080/baloot/error");
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        Amazon amazon;
        try {
            amazon = Amazon.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            if(!amazon.isAnybodyLoggedIn()) {
                response.sendRedirect("http://localhost:8080/baloot/Login");
                return;
            }
            if(Objects.equals(action, "rate"))
                amazon.addRating(request.getParameter("quantity"));
            if(Objects.equals(action, "comment"))
                amazon.addComment(request.getParameter("comment"));
            if(Objects.equals(action, "add"))
                amazon.addToBuyList();
            if(Objects.equals(action, "like"))
                amazon.rateComment(request.getParameter("comment_id"), "1");
            if(Objects.equals(action, "dislike"))
                amazon.rateComment(request.getParameter("comment_id"), "-1");
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Commodity.jsp");
            requestDispatcher.forward(request, response);
        } catch (Exception e) {
            response.sendRedirect("http://localhost:8080/baloot/error");
        }
    }
}