package all.Controller;

import all.domain.Amazon.Amazon;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AddCreditServlet", urlPatterns = "/credit")
public class AddCreditServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("Credit.jsp");
        requestDispatcher.forward(request, response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Amazon amazon = null;
        try {
            amazon = Amazon.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int credit = Integer.parseInt(request.getParameter("credit"));
        try {
            if(amazon.isAnybodyLoggedIn())
                amazon.increaseCredit(amazon.getActiveUser(), credit);
            else
                response.sendRedirect("http://localhost:8080/baloot/Login");
        } catch (Exception e) {
            response.sendRedirect("http://localhost:8080/baloot/error");
        }
        response.sendRedirect("http://localhost:8080/baloot");
    }
}
