//package com.baloot.IE.Controller;
//
//import com.baloot.IE.domain.Amazon.Amazon;
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//
//@WebServlet(name = "BuyListServlet", urlPatterns = "/buyList")
//public class BuyListServlet extends HttpServlet {
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Amazon amazon = null;
//        try {
//            amazon = Amazon.getInstance();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        if(amazon.isAnybodyLoggedIn()) {
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher("BuyList.jsp");
//            requestDispatcher.forward(request, response);
//        } else {
//            response.sendRedirect("http://localhost:8080/baloot/login");
//        }
//    }
//}
