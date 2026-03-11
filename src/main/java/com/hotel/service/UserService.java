package com.hotel.service;

import java.util.List;

import com.hotel.dao.UserDAO;
import com.hotel.model.User;
import com.hotel.util.PasswordUtil;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    // ===============================
    // REGISTER BUSINESS LOGIC
    // ===============================
    public boolean registerUser(User user) {
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        return userDAO.insertUser(user);
    }
    
    // ===============================
    // LOGIN BUSINESS LOGIC
    // ===============================
    public User login(String email, String password) {
        User user = userDAO.getUserByEmail(email);
        if (user != null && PasswordUtil.checkPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    // ===============================
    // GET USER BY ID
    // ===============================
    public User getUserById(int userId) {
        if(userId <= 0) {
            return null; // Invalid user ID
        }else{
            return userDAO.getUserById(userId);
        }
    }

    // ===============================
    // GET USER BY EMAIL
    // ===============================
    public User getUserByEmail(String email) {
        if(email == null || email.isEmpty()) {
            return null; // Invalid email
        }else{
            return userDAO.getUserByEmail(email);
        }
    }

    // ===============================
    // GET ALL USERS
    // ===============================
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    // ===============================
    // UPDATE USER (Admin Only)
    // ===============================
    public boolean updateUser(User user) {

        // Business rule:
        // Role cannot be empty
        if (user.getRole() == null || user.getRole().isEmpty()) {
            return false;
        }

        return userDAO.updateUser(user);
    }

    // ===============================
    // DELETE USER (Admin Only)
    // ===============================
    public boolean deleteUser(int userId) {
        return userDAO.deleteUser(userId);
    }
}