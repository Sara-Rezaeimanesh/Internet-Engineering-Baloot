package com.baloot.IE.FIlter;

import com.google.gson.JsonObject;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(filterName = "ErrorFilter", urlPatterns = {"/*"})
@Order(2)
public class ErrorFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            int statusCode = response.getStatus();
            if (statusCode >= 400 && statusCode < 600) {
                response.setStatus(statusCode);
            } else {
                if (e instanceof IllegalArgumentException) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            }
            response.setContentType("application/json");

            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("message", "An error occurred");
            errorJson.addProperty("exceptionType", e.getClass().getName());
            errorJson.addProperty("exceptionMessage", e.getMessage());

            PrintWriter out = response.getWriter();
            out.print(errorJson);
            out.flush();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code
    }

    @Override
    public void destroy() {
        // Cleanup code
    }
}
