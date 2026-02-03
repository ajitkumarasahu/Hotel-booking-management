package com.osrtc.bustracking.model;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Represents a GPS location record for a bus in the OSRTC Bus Tracking System.
 * Each Location is linked to a specific Bus and contains latitude, longitude, and timestamp information.
 */
public class Location {

    private static final Logger logger = Logger.getLogger(Location.class.getName());

    /** Unique identifier for the location record */
    private int locationId;

    /** ID of the bus this location belongs to */
    private int busId;

    /** Latitude coordinate of the bus */
    private double latitude;

    /** Longitude coordinate of the bus */
    private double longitude;

    /** Timestamp when the location was recorded */
    private Date timestamp;

    /** Gets the unique location ID. */
    public int getLocationId() {
        logger.info("Getting locationId: " + locationId);
        return locationId;
    }

    /** Sets the unique location ID. */
    public void setLocationId(int locationId) {
        logger.info("Setting locationId: " + locationId);
        this.locationId = locationId;
    }

    /** Gets the bus ID associated with this location. */
    public int getBusId() {
        logger.info("Getting busId: " + busId);
        return busId;
    }

    /** Sets the bus ID associated with this location. */
    public void setBusId(int busId) {
        logger.info("Setting busId: " + busId);
        this.busId = busId;
    }

    /** Gets the latitude coordinate of the bus. */
    public double getLatitude() {
        logger.info("Getting latitude: " + latitude);
        return latitude;
    }

    /** Sets the latitude coordinate of the bus. */
    public void setLatitude(double latitude) {
        logger.info("Setting latitude: " + latitude);
        this.latitude = latitude;
    }

    /** Gets the longitude coordinate of the bus. */
    public double getLongitude() {
        logger.info("Getting longitude: " + longitude);
        return longitude;
    }

    /** Sets the longitude coordinate of the bus. */
    public void setLongitude(double longitude) {
        logger.info("Setting longitude: " + longitude);
        this.longitude = longitude;
    }

    /** Gets the timestamp when this location was recorded. */
    public Date getTimestamp() {
        logger.info("Getting timestamp: " + timestamp);
        return timestamp;
    }

    /** Sets the timestamp when this location was recorded. */
    public void setTimestamp(Date timestamp) {
        logger.info("Setting timestamp: " + timestamp);
        this.timestamp = timestamp;
    }
}
