package all.Controller;

import java.io.*;

import all.domain.Amazon.Amazon;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "voteCommentServlet", urlPatterns = "/voteComment")
public class voteCommentServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        try {
            Amazon amazon = Amazon.getInstance();
            String commentId = pathParts[1];
            String vote = pathParts[2];
            amazon.rateComment(commentId, vote);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("Commodity.jsp");
        requestDispatcher.forward(request, response);
    }
}