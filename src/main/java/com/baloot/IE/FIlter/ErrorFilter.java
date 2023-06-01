package com.baloot.IE.FIlter;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@WebFilter(filterName = "ErrorFilter", urlPatterns = {"/*"})
public class ErrorFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            filterChain.doFilter(servletRequest, servletResponse);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            int statusCode = response.getStatus();
            if (statusCode >= 400 && statusCode < 600) {
                response.setStatus(statusCode);
            } else {
                if (e instanceof UnauthorizedException) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                } else if(e instanceof IllegalArgumentException) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            }
            response.setContentType("application/json");
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("code", statusCode);
            errorJson.addProperty("message", e.getMessage());
            errorJson.addProperty("more info", "imaginary.website.com");

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
