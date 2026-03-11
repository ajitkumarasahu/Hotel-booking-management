package com.hotel.controller;

import com.hotel.model.User;
import com.hotel.service.UserService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List; 

import javax.annotation.processing.Generated;

@WebServlet("/users/*")
@Produces(MediaType.APPLICATION_JSON)  
@Consumes(MediaType.APPLICATION_JSON) 
public class UserController extends HttpServlet {

    private UserService userService = new UserService();

    // ===============================
    // GET - Fetch User by ID 
    // ===============================
    @Generated(value = "UserController - GET /users/{id}")
    protected void GetbyId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Read query parameters
        String userIdParam = request.getParameter("userId");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");

        JSONObject json = new JSONObject();

        if (userIdParam != null) {
            int userId = Integer.parseInt(userIdParam);
            json.put("userId", userId);
        }

        if (name != null) {
            json.put("name", name);
        }

        if (email != null) {
            json.put("email", email);
        }
        if (phone != null) {
            json.put("phone", phone);
        }

        if (role != null) {
            json.put("role", role);
        }

        out.print(json.toString());

        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                int userId = Integer.parseInt(pathInfo.substring(1));
                User user = userService.getUserById(userId);
                if (user != null) {
                    JSONObject obj = new JSONObject();
                    obj.put("userId", user.getUserId());
                    obj.put("name", user.getName());
                    obj.put("email", user.getEmail());
                    obj.put("phone", user.getPhone());
                    obj.put("role", user.getRole());
                    out.print(obj.toString());
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"message\":\"User not found\"}");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"message\":\"Invalid user ID format\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"message\":\"User ID required in URL\"}");
        }
    }

    // ===============================
    // GET - Fetch User by Email
    // ===============================
    @Generated(value = "UserController - GET /users?email={email}")
    protected void GetByEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String email = request.getParameter("email");

        if (email != null && !email.isEmpty()) {
            User user = userService.getUserByEmail(email);
            if (user != null) {
                JSONObject obj = new JSONObject();
                obj.put("userId", user.getUserId());
                obj.put("name", user.getName());
                obj.put("email", user.getEmail());
                obj.put("phone", user.getPhone());
                obj.put("role", user.getRole());
                out.print(obj.toString());
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"message\":\"User not found\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"message\":\"Email parameter required\"}");
        }
    }

    // ===============================
    // GET - Fetch All Users
    // ===============================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Check if specific query parameters are present to route to the correct method
        String userIdParam = request.getParameter("userId");
        String emailParam = request.getParameter("email");
        if (userIdParam != null) {
            GetbyId(request, response);
            return;
        } else if (emailParam != null) {
            GetByEmail(request, response);
            return;
        }

        //Read query parameters
        String userId = request.getParameter("userId");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");
        
        JSONObject json = new JSONObject();
        
        if (userId != null) {
            json.put("userId", userId);
        }
        if (name != null) {
            json.put("name", name);
        }   
        if (email != null) {
            json.put("email", email);
        }
        if (phone != null) {
            json.put("phone", phone);
        }
        if (role != null) {
            json.put("role", role);
        }

        List<User> users = userService.getAllUsers();

        JSONArray jsonArray = new JSONArray();

        for (User user : users) {
            JSONObject obj = new JSONObject();
            obj.put("userId", user.getUserId());
            obj.put("name", user.getName());
            obj.put("email", user.getEmail());
            obj.put("phone", user.getPhone());
            obj.put("role", user.getRole());
            jsonArray.put(obj);
        }

        out.print(jsonArray.toString());
    }

    // ===============================
    // PUT - Update User
    // ===============================
    @Generated(value = "UserController - PUT /users/{id}")
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = request.getReader().readLine()) != null) {
            sb.append(line);
        }

        JSONObject json = new JSONObject(sb.toString());

        User user = new User();
        user.setUserId(json.getInt("userId"));
        user.setName(json.getString("name"));
        user.setPhone(json.getString("phone"));
        user.setRole(json.getString("role"));

        boolean updated = userService.updateUser(user);

        if (updated) {
            out.print("{\"message\":\"User updated successfully\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"message\":\"Update failed\"}");
        }
    }

    // ===============================
    // DELETE - Delete User
    // ===============================
    @Generated(value = "UserController - DELETE /users/{id}")
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String userIdParam = request.getParameter("userId");

        if (userIdParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"message\":\"User ID required\"}");
            return;
        }

        int userId = Integer.parseInt(userIdParam);

        boolean deleted = userService.deleteUser(userId);

        if (deleted) {
            out.print("{\"message\":\"User deleted successfully\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"message\":\"Delete failed\"}");
        }
    }

}