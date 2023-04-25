//package com.baloot.IE.Controller;
//import java.io.*;
//
//import com.baloot.IE.domain.Amazon.Amazon;
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.*;
//import jakarta.servlet.annotation.*;
//
//@WebServlet(name = "LoginServlet", urlPatterns = "/login")
//public class LoginServlet extends HttpServlet {
//
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        RequestDispatcher requestDispatcher = request.getRequestDispatcher("login.jsp");
//        requestDispatcher.forward(request, response);
//    }
//
//    public void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String userName = request.getParameter("uesrname");
//        String userPass = request.getParameter("Password");
//        Amazon amazon = null;
//        try {
//            amazon = Amazon.getInstance();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        if(amazon.DoesUserExist(userName, userPass)) {
//            amazon.setActiveUser(userName);
//            response.sendRedirect("http://localhost:8080/baloot");
//        } else {
//            response.sendRedirect("http://localhost:8080/baloot/login");
//        }
//    }
//
//}