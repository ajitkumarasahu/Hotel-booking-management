// Defines the package where this class belongs
package com.hotel.controller;

// Import required classes for IO operations
import java.io.IOException;
import java.io.PrintWriter;

// Import servlet-related classes
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Import date and time classes
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Maps this servlet to the URL "/test"
@WebServlet("/test")
public class TestController extends HttpServlet {

    // Handles HTTP GET requests
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set response type to JSON
        response.setContentType("application/json");

        // Get writer object to send response to client
        PrintWriter out = response.getWriter();

        // Get current date (only date, no time)
        LocalDate currentDate = LocalDate.now();

        // Get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Create formatter for date-time in desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        // Format current date-time using formatter
        String formattedDateTime = currentDateTime.format(formatter);

        // Start writing JSON response
        out.println("{");
        out.println("\"status\": \"success\",");  // API status
        out.println("\"message\": \"API is working correctly\",");  // Message
        out.println("\"date\": \"" + currentDate + "\",");  // Current date
        out.println("\"month\": \"" + currentDate.getMonth() + "\",");  // Month name
        out.println("\"dateTime\": \"" + formattedDateTime + "\"");  // Formatted date-time
        out.println("}");
    }
}