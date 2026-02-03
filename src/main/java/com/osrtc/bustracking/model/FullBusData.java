package com.osrtc.bustracking.model;

import java.util.logging.Logger;

/**
 * Aggregates all related entities for a bus in the OSRTC Bus Tracking System.
 * Contains Bus, Route, Driver, Location, and Admin information.
 */
public class FullBusData {

    private static final Logger logger = Logger.getLogger(FullBusData.class.getName());

    /** The bus entity */
    private Bus bus;

    /** The route entity assigned to the bus */
    private Route route;

    /** The driver assigned to the bus */
    private Driver driver;

    /** The current location of the bus */
    private Location location;

    /** The admin responsible for managing this bus */
    private Admin admin;

    /** Gets the bus entity. */
    public Bus getBus() {
        logger.info("Getting Bus entity: " + bus);
        return bus;
    }

    /** Sets the bus entity. */
    public void setBus(Bus bus) {
        logger.info("Setting Bus entity: " + bus);
        this.bus = bus;
    }

    /** Gets the route entity. */
    public Route getRoute() {
        logger.info("Getting Route entity: " + route);
        return route;
    }

    /** Sets the route entity. */
    public void setRoute(Route route) {
        logger.info("Setting Route entity: " + route);
        this.route = route;
    }

    /** Gets the driver entity. */
    public Driver getDriver() {
        logger.info("Getting Driver entity: " + driver);
        return driver;
    }

    /** Sets the driver entity. */
    public void setDriver(Driver driver) {
        logger.info("Setting Driver entity: " + driver);
        this.driver = driver;
    }

    /** Gets the location entity. */
    public Location getLocation() {
        logger.info("Getting Location entity: " + location);
        return location;
    }

    /** Sets the location entity. */
    public void setLocation(Location location) {
        logger.info("Setting Location entity: " + location);
        this.location = location;
    }

    /** Gets the admin entity. */
    public Admin getAdmin() {
        logger.info("Getting Admin entity: " + admin);
        return admin;
    }

    /** Sets the admin entity. */
    public void setAdmin(Admin admin) {
        logger.info("Setting Admin entity: " + admin);
        this.admin = admin;
    }
}
