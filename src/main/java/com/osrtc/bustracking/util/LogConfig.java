package com.osrtc.bustracking.util;

import java.io.InputStream;
import java.util.logging.LogManager;

public class LogConfig {

    static {
        try (InputStream is =
                     LogConfig.class.getClassLoader()
                             .getResourceAsStream("logging.properties")) {

            if (is != null) {
                LogManager.getLogManager().readConfiguration(is);
            } else {
                System.err.println("logging.properties not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

