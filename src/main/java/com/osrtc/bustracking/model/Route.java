package com.osrtc.bustracking.model;

import java.util.logging.Logger;

/**
 * Represents a bus route in the OSRTC Bus Tracking System.
 * A Route contains information about the start and end points, distance, and estimated travel time.
 */
public class Route {

    private static final Logger logger = Logger.getLogger(Route.class.getName());

    /** Unique identifier for the route */
    private int routeId;

    /** Starting point of the route */
    private String startPoint;

    /** Ending point of the route */
    private String endPoint;

    /** Total distance of the route in kilometers */
    private double totalDistanceKm;

    /** Estimated travel time for the route (e.g., "2h 30m") */
    private String estimatedTime;

    /** Gets the unique route ID. */
    public int getRouteId() {
        logger.info("Getting routeId: " + routeId);
        return routeId;
    }

    /** Sets the unique route ID. */
    public void setRouteId(int routeId) {
        logger.info("Setting routeId: " + routeId);
        this.routeId = routeId;
    }

    /** Gets the starting point of the route. */
    public String getStartPoint() {
        logger.info("Getting startPoint: " + startPoint);
        return startPoint;
    }

    /** Sets the starting point of the route. */
    public void setStartPoint(String startPoint) {
        logger.info("Setting startPoint: " + startPoint);
        this.startPoint = startPoint;
    }

    /** Gets the ending point of the route. */
    public String getEndPoint() {
        logger.info("Getting endPoint: " + endPoint);
        return endPoint;
    }

    /** Sets the ending point of the route. */
    public void setEndPoint(String endPoint) {
        logger.info("Setting endPoint: " + endPoint);
        this.endPoint = endPoint;
    }

    /** Gets the total distance of the route in kilometers. */
    public double getTotalDistanceKm() {
        logger.info("Getting totalDistanceKm: " + totalDistanceKm);
        return totalDistanceKm;
    }

    /** Sets the total distance of the route in kilometers. */
    public void setTotalDistanceKm(double totalDistanceKm) {
        logger.info("Setting totalDistanceKm: " + totalDistanceKm);
        this.totalDistanceKm = totalDistanceKm;
    }

    /** Gets the estimated travel time for the route. */
    public String getEstimatedTime() {
        logger.info("Getting estimatedTime: " + estimatedTime);
        return estimatedTime;
    }

    /** Sets the estimated travel time for the route. */
    public void setEstimatedTime(String estimatedTime) {
        logger.info("Setting estimatedTime: " + estimatedTime);
        this.estimatedTime = estimatedTime;
    }
}
