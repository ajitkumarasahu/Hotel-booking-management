package com.hotel.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter("/api/*")
public class RoleFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();
        String role = (String) req.getAttribute("userRole");

        // ADMIN ONLY APIs
        if(path.contains("/hotels") || path.contains("/rooms") || path.contains("/dashboard")){

            if(role == null || !role.equals("ADMIN")){
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                res.getWriter().write("Access Denied: Admin Only");
                return;
            }
        }

        // USER APIs
        if(path.contains("/bookings")){

            if(role == null){
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().write("Unauthorized");
                return;
            }
        }

        chain.doFilter(request,response);
    }
}