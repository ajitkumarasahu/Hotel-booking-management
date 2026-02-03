package com.osrtc.bustracking.util;
// Declares that this class is part of the utility package

import java.io.InputStream;
import java.util.logging.LogManager;
// Imports classes for reading streams and configuring Java logging

public class LogConfig {

    // Static block is executed once when the class is loaded
    static {
        try (InputStream is =
                     LogConfig.class.getClassLoader()
                             .getResourceAsStream("logging.properties")) {
            // Attempt to load "logging.properties" from the classpath

            if (is != null) {
                // If the file is found, read the configuration into LogManager
                LogManager.getLogManager().readConfiguration(is);
            } else {
                // Print error if logging.properties is not found
                System.err.println("logging.properties not found");
            }

        } catch (Exception e) {
            // Print stack trace if any exception occurs while loading config
            e.printStackTrace();
        }
    }
}
