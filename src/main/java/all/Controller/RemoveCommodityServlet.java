package all.Controller;

import all.domain.Amazon.Amazon;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RemoveCommodityServlet", urlPatterns = "/remove")
public class RemoveCommodityServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Amazon amazon = null;
        try {
            amazon = Amazon.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String commodityId = request.getParameter("commodityId");
        try {
            amazon.removeFromBuyList(amazon.getActiveUser(), Integer.parseInt(commodityId));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        response.sendRedirect("http://localhost:8080/baloot/buyList");
    }
}
