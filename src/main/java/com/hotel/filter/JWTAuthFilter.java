package com.hotel.filter;

import com.hotel.util.JWTUtil;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/api/*")
public class JWTAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain)throws IOException, ServletException {
            
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        // Allow auth endpoints
        if(path.contains("/auth/")){
            chain.doFilter(request,response);
            return;
        }

        String header = req.getHeader("Authorization");

        if(header == null || !header.startsWith("Bearer ")){
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Missing Token");
            return;
        }

        String token = header.substring(7);
        
        String role = JWTUtil.getRole(token);

        // store in request
        req.setAttribute("userRole", role);

        if(JWTUtil.validateToken(token) == null){
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Invalid Token");
            return;
        }

        chain.doFilter(request,response);
    }
}