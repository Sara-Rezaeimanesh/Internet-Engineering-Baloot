package all.Controller;

import all.domain.Amazon.Amazon;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RemoveCommodityServlet", urlPatterns = "/remove/*")
public class RemoveCommodityServlet extends HttpServlet {

    public void doSth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Amazon amazon = null;
        try {
            amazon = Amazon.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        int commodityId = Integer.parseInt(pathParts[1]);

        try {
            amazon.removeFromBuyList(amazon.getActiveUser(), commodityId);
        } catch (Exception e) {
            Amazon.setErrorMsg(e.getMessage());
            response.sendRedirect("http://localhost:8080/baloot/error");
            return;
        }
        response.sendRedirect("http://localhost:8080/baloot/buyList");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.doSth(request, response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.doSth(request, response);
    }
}
