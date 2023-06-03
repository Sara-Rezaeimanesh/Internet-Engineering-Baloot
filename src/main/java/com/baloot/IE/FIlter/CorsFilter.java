package com.baloot.IE.FIlter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@WebFilter(filterName = "CorsFilter", urlPatterns = "/*")
public class CorsFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", String.valueOf(true));
        System.out.println("hi 2");
        if (httpServletRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        } else chain.doFilter(request, response);
    }
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }
}
