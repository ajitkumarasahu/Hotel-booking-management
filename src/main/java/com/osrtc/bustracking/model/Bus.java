package com.osrtc.bustracking.model;

import java.util.logging.Logger;

/**
 * Represents a Bus entity in the OSRTC Bus Tracking System.
 * Stores bus details including its route, driver, and operational status.
 */
public class Bus {

    private static final Logger logger = Logger.getLogger(Bus.class.getName());

    /** Unique identifier for the bus */
    private int busId;

    /** Bus number or code for identification */
    private String busNumber;

    /** Associated route ID */
    private int routeId;

    /** Assigned driver ID */
    private int driverId;

    /** Current operational status of the bus (e.g., active, inactive) */
    private String status;

    /** Gets the unique bus ID. */
    public int getBusId() {
        logger.info("Getting busId: " + busId);
        return busId;
    }

    /** Sets the unique bus ID. */
    public void setBusId(int busId) {
        logger.info("Setting busId: " + busId);
        this.busId = busId;
    }

    /** Gets the bus number. */
    public String getBusNumber() {
        logger.info("Getting busNumber: " + busNumber);
        return busNumber;
    }

    /** Sets the bus number. */
    public void setBusNumber(String busNumber) {
        logger.info("Setting busNumber: " + busNumber);
        this.busNumber = busNumber;
    }

    /** Gets the associated route ID. */
    public int getRouteId() {
        logger.info("Getting routeId: " + routeId);
        return routeId;
    }

    /** Sets the associated route ID. */
    public void setRouteId(int routeId) {
        logger.info("Setting routeId: " + routeId);
        this.routeId = routeId;
    }

    /** Gets the assigned driver ID. */
    public int getDriverId() {
        logger.info("Getting driverId: " + driverId);
        return driverId;
    }

    /** Sets the assigned driver ID. */
    public void setDriverId(int driverId) {
        logger.info("Setting driverId: " + driverId);
        this.driverId = driverId;
    }

    /** Gets the current status of the bus. */
    public String getStatus() {
        logger.info("Getting status: " + status);
        return status;
    }

    /** Sets the current status of the bus. */
    public void setStatus(String status) {
        logger.info("Setting status: " + status);
        this.status = status;
    }
}
