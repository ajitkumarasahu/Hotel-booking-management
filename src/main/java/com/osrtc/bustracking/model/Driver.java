package com.osrtc.bustracking.model;

import java.util.logging.Logger;

/**
 * Represents a Driver entity in the OSRTC Bus Tracking System.
 * Stores driver details such as name, contact, license, and current status.
 */
public class Driver {

    private static final Logger logger = Logger.getLogger(Driver.class.getName());

    /** Unique identifier for the driver */
    private int driverId;

    /** Full name of the driver */
    private String driverName;

    /** Contact phone number of the driver */
    private String phone;

    /** Driver's license number */
    private String licenseNumber;

    /** Current status of the driver (e.g., active, inactive) */
    private String status;

    /** Gets the unique driver ID. */
    public int getDriverId() {
        logger.info("Getting driverId: " + driverId);
        return driverId;
    }

    /** Sets the unique driver ID. */
    public void setDriverId(int driverId) {
        logger.info("Setting driverId: " + driverId);
        this.driverId = driverId;
    }

    /** Gets the driver's full name. */
    public String getDriverName() {
        logger.info("Getting driverName: " + driverName);
        return driverName;
    }

    /** Sets the driver's full name. */
    public void setDriverName(String driverName) {
        logger.info("Setting driverName: " + driverName);
        this.driverName = driverName;
    }

    /** Gets the driver's phone number. */
    public String getPhone() {
        logger.info("Getting phone: " + phone);
        return phone;
    }

    /** Sets the driver's phone number. */
    public void setPhone(String phone) {
        logger.info("Setting phone: " + phone);
        this.phone = phone;
    }

    /** Gets the driver's license number. */
    public String getLicenseNumber() {
        logger.info("Getting licenseNumber: " + licenseNumber);
        return licenseNumber;
    }

    /** Sets the driver's license number. */
    public void setLicenseNumber(String licenseNumber) {
        logger.info("Setting licenseNumber: " + licenseNumber);
        this.licenseNumber = licenseNumber;
    }

    /** Gets the current status of the driver. */
    public String getStatus() {
        logger.info("Getting status: " + status);
        return status;
    }

    /** Sets the current status of the driver. */
    public void setStatus(String status) {
        logger.info("Setting status: " + status);
        this.status = status;
    }
}
