package all.Controller;

import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

import all.domain.Amazon.Amazon;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("login.jsp");
        requestDispatcher.forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userName = request.getParameter("username");
        String userPass = request.getParameter("Password");
        try {
            Amazon amazon = Amazon.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (bolbolestan.doesStudentExist(studentId)) {
            bolbolestan.makeLoggedIn(studentId);
            response.sendRedirect("http://localhost:8080/ca3_war_exploded");
        } else {
            response.sendRedirect("http://localhost:8080/ca3_war_exploded/login");
        }
    }

}