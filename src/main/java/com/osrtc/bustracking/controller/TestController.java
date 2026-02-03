// Package declaration: groups related classes together
package com.osrtc.bustracking.controller;

/**
 * TestController
 * -------------------------
 * This REST controller is used to test whether the Controller layer
 * of the application is working correctly.
 *
 * It exposes a GET API that returns:
 *  - Current server date & time
 *  - Current day name (English)
 *  - Current month name in Odia language
 *
 * Response format: JSON
 */

// Import annotations for REST API
import javax.ws.rs.GET;                       // Annotation for HTTP GET method
import javax.ws.rs.Path;                      // Defines URI path for the controller
import javax.ws.rs.Produces;                  // Specifies response media type
import javax.ws.rs.core.MediaType;            // Contains media type constants

// Import Java time API classes
import java.time.LocalDateTime;               // Used to fetch current date & time
import java.time.format.DateTimeFormatter;    // Used to format date & time
import java.time.DayOfWeek;                   // Used to get day name
import java.time.Month;                       // Used to get month enum

// Import utility classes
import java.util.HashMap;                     // Implementation of Map
import java.util.Map;                         // Interface for key-value pairs
import java.util.logging.Logger;              // Used for logging

/**
 * REST Controller class
 * URL Mapping: /api/test
 */
@Path("/test") // Base path for this controller
public class TestController {

    // Logger instance for logging messages
    private static final Logger logger = Logger.getLogger(TestController.class.getName());

    /**
     * testAPI()
     * -------------------------
     * Handles HTTP GET requests.
     *
     * @return JSON string containing:
     *         - status
     *         - message
     *         - serverTime
     *         - dayName
     *         - monthOdia
     */
    @GET // Marks this method as handling HTTP GET requests
    @Produces(MediaType.APPLICATION_JSON) // Response type is JSON
    public String testAPI() {

        // Log API call
        logger.info("testAPI() called");

        // Fetch current server date and time
        LocalDateTime now = LocalDateTime.now();

        // Log current server time
        logger.info("Current server time fetched: " + now);

        // Create formatter to format date and time as dd-MM-yyyy HH:mm:ss
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        // Log formatter initialization
        logger.info("DateTimeFormatter initialized");

        /**
         * Map to store Odia month names
         * Key   -> Month enum (JANUARY, FEBRUARY, etc.)
         * Value -> Odia month name
         */
        Map<Month, String> odiaMonths = new HashMap<>();
        odiaMonths.put(Month.JANUARY, "ଜାନୁଆରୀ");
        odiaMonths.put(Month.FEBRUARY, "ଫେବୃଆରୀ");
        odiaMonths.put(Month.MARCH, "ମାର୍ଚ୍ଚ");
        odiaMonths.put(Month.APRIL, "ଏପ୍ରିଲ");
        odiaMonths.put(Month.MAY, "ମେ");
        odiaMonths.put(Month.JUNE, "ଜୁନ");
        odiaMonths.put(Month.JULY, "ଜୁଲାଇ");
        odiaMonths.put(Month.AUGUST, "ଅଗଷ୍ଟ");
        odiaMonths.put(Month.SEPTEMBER, "ସେପ୍ଟେମ୍ବର");
        odiaMonths.put(Month.OCTOBER, "ଅକ୍ଟୋବର");
        odiaMonths.put(Month.NOVEMBER, "ନଭେମ୍ବର");
        odiaMonths.put(Month.DECEMBER, "ଡିସେମ୍ବର");

        // Log that Odia month map is ready
        logger.info("Odia month map initialized");

        // Get current month name in Odia using the map
        String odiaMonthName = odiaMonths.get(now.getMonth());

        // Log resolved Odia month name
        logger.info("Odia month resolved: " + odiaMonthName);

        // Get current day of the week (MONDAY, TUESDAY, etc.)
        DayOfWeek dayOfWeek = now.getDayOfWeek();

        // Convert day name to readable format (Monday, Tuesday, etc.)
        String dayName = dayOfWeek.toString().substring(0, 1)
                + dayOfWeek.toString().substring(1).toLowerCase();

        // Log formatted day name
        logger.info("Formatted day name: " + dayName);

        // Build JSON response manually as a string
        String response = "{"
                + "\"status\":\"SUCCESS\","
                + "\"message\":\"Controller Layer Working Successfully\","
                + "\"serverTime\":\"" + now.format(formatter) + "\","
                + "\"dayName\":\"" + dayName + "\","
                + "\"monthOdia\":\"" + odiaMonthName + "\""
                + "}";

        // Log generated response
        logger.info("Response generated: " + response);

        // Return JSON response
        return response;
    }
}
