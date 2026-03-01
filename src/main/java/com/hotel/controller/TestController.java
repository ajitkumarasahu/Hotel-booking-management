package com.hotel.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/test")
public class TestController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
            
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        // Get current date
        LocalDate currentDate = LocalDate.now();

        // Get current date & time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Format date & time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        out.println("{");
        out.println("\"status\": \"success\",");
        out.println("\"message\": \"API is working correctly\",");
        out.println("\"date\": \"" + currentDate + "\",");
        out.println("\"month\": \"" + currentDate.getMonth() + "\",");
        out.println("\"dateTime\": \"" + formattedDateTime + "\"");
        out.println("}");
    }
}