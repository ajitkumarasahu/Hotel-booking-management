package com.hotel.controller;

import com.hotel.model.User;
import com.hotel.service.UserService;
import com.hotel.util.JWTUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;

@WebServlet("/auth/*")
public class AuthController extends HttpServlet {

    private UserService userService = new UserService();

    // REGISTER
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = request.getPathInfo();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if ("/register".equals(path)) {

            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject json = new JSONObject(sb.toString());

            User user = new User(
                    json.getString("name"),
                    json.getString("email"),
                    json.getString("password"),
                    json.getString("phone"),
                    "USER"
            );

            boolean success = userService.registerUser(user);

            if (success) {
                out.print("{\"message\":\"User registered successfully\"}");
            } else {
                out.print("{\"message\":\"Registration failed\"}");
            }
        }

        if ("/login".equals(path)) {

            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject json = new JSONObject(sb.toString());

            User user = userService.login(
                    json.getString("email"),
                    json.getString("password")
            );

            if (user != null) {
                String token = JWTUtil.generateToken(user.getEmail(), user.getRole());
                out.print("{\"token\":\"" + token + "\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"message\":\"Invalid credentials\"}");
            }
        }
    }

}