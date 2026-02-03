package com.osrtc.bustracking.dao; 
// Defines the package where this class belongs.

import com.osrtc.bustracking.model.Bus; 
// Imports the Bus model class used to represent bus objects.

import com.osrtc.bustracking.util.DBConnection; 
// Imports a utility class for getting database connections.

import java.sql.*; 
// Imports classes for JDBC database operations: Connection, PreparedStatement, ResultSet, etc.

import java.util.*; 
// Imports utility classes like List, ArrayList.

import java.util.logging.Logger; // ✅ ADDED
// Imports Java's built-in logging utility for logging info, warnings, and errors.

public class BusDAO { 
    // Data Access Object (DAO) class for Bus-related database operations.

    private static final Logger logger = Logger.getLogger(BusDAO.class.getName()); // ✅ ADDED
    // Logger instance for logging messages in this class.

    public List<Bus> getAllBuses() { 
        logger.info("BusDAO.getAllBuses() called"); // ✅ ADDED
        List<Bus> list = new ArrayList<>(); 
        // Create a list to store all buses fetched from the database.

        try (Connection conn = DBConnection.getConnection()) { 
            // Open a database connection using a try-with-resources (auto-closeable).
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM bus"); 
            // Execute a SQL query to get all records from the "bus" table.

            while (rs.next()) { 
                // Iterate through each row in the result set.
                Bus b = new Bus(); 
                // Create a new Bus object for each row.

                b.setBusId(rs.getInt("bus_id"));
                b.setBusNumber(rs.getString("bus_number"));
                b.setRouteId(rs.getInt("route_id"));
                b.setDriverId(rs.getInt("driver_id"));
                b.setStatus(rs.getString("status"));
                // Set properties of the Bus object from the database row.

                list.add(b); 
                // Add the Bus object to the list.
            }

            logger.info("Total buses fetched: " + list.size()); // ✅ ADDED
            // Log the number of buses retrieved.
        } catch (Exception e) { 
            logger.severe("Error in getAllBuses(): " + e.getMessage()); // ✅ ADDED
            e.printStackTrace(); 
            // Log any exceptions and print the stack trace.
        }

        return list; 
        // Return the list of Bus objects (empty if none found or exception occurred).
    }

    public Bus getBusById(int id) { 
        logger.info("BusDAO.getBusById() called with id=" + id); // ✅ ADDED
        Bus b = null; 
        // Initialize Bus object to null (will be returned if not found).

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM bus WHERE bus_id=?"); 
            // Prepare SQL query with parameter to fetch bus by ID.

            ps.setInt(1, id); 
            // Set the bus_id parameter in the query.

            ResultSet rs = ps.executeQuery(); 
            // Execute the query.

            if (rs.next()) { 
                // If a record exists:
                b = new Bus(); 
                b.setBusId(rs.getInt("bus_id"));
                b.setBusNumber(rs.getString("bus_number"));
                b.setRouteId(rs.getInt("route_id"));
                b.setDriverId(rs.getInt("driver_id"));
                b.setStatus(rs.getString("status"));
                // Map database columns to Bus object.

                logger.info("Bus found: ID=" + id); // ✅ ADDED
            } else {
                logger.warning("Bus not found: ID=" + id); // ✅ ADDED
            }
        } catch (Exception e) {
            logger.severe("Error in getBusById(): " + e.getMessage()); // ✅ ADDED
            e.printStackTrace();
        }

        return b; 
        // Return the Bus object or null if not found.
    }

    public boolean addBus(Bus b) { 
        logger.info("BusDAO.addBus() called for busNumber=" + b.getBusNumber()); // ✅ ADDED

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO bus(bus_number, route_id, driver_id, status) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            // Prepare SQL insert query and request generated keys (auto-incremented ID).

            ps.setString(1, b.getBusNumber());
            ps.setInt(2, b.getRouteId());
            ps.setInt(3, b.getDriverId());
            ps.setString(4, b.getStatus());
            // Set the values from the Bus object.

            int affected = ps.executeUpdate(); 
            // Execute insert and get number of affected rows.

            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        b.setBusId(keys.getInt(1)); 
                        logger.info("Bus added successfully: ID=" + b.getBusId()); // ✅ ADDED
                    }
                }
            } else {
                logger.warning("Failed to add bus: " + b.getBusNumber()); // ✅ ADDED
            }

            return affected > 0; 
            // Return true if insert was successful.
        } catch (Exception e) {
            logger.severe("Error in addBus(): " + e.getMessage()); // ✅ ADDED
            e.printStackTrace();
        }

        return false; 
        // Return false if exception occurred.
    }

    public boolean multipleBusInsert(List<Bus> busList) { 
        logger.info("BusDAO.multipleBusInsert() called for " + busList.size() + " buses"); // ✅ ADDED

        String sql = "INSERT INTO bus (bus_number, route_id, driver_id, status) VALUES (?, ?, ?, ?)"; 

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Bus b : busList) {
                ps.setString(1, b.getBusNumber());
                ps.setInt(2, b.getRouteId());
                ps.setInt(3, b.getDriverId());
                ps.setString(4, b.getStatus());
                ps.addBatch(); 
                // Add each bus to batch insert.
            }

            int[] results = ps.executeBatch(); 
            // Execute batch insert and get results array.

            for (int r : results) {
                if (r == 0) { 
                    // If any insert failed:
                    logger.warning("One of the bus inserts failed"); // ✅ ADDED
                    return false;
                }
            }

            logger.info("All buses inserted successfully"); // ✅ ADDED
            return true;

        } catch (Exception e) {
            logger.severe("Error in multipleBusInsert(): " + e.getMessage()); // ✅ ADDED
            e.printStackTrace();
        }

        return false; 
    }

    public boolean updateBus(Bus b) { 
        logger.info("BusDAO.updateBus() called for ID=" + b.getBusId()); // ✅ ADDED

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE bus SET bus_number=?, route_id=?, driver_id=?, status=? WHERE bus_id=?");

            ps.setString(1, b.getBusNumber());
            ps.setInt(2, b.getRouteId());
            ps.setInt(3, b.getDriverId());
            ps.setString(4, b.getStatus());
            ps.setInt(5, b.getBusId());

            boolean updated = ps.executeUpdate() > 0; 
            // Returns true if update affected any row.

            if (updated) {
                logger.info("Bus updated successfully: ID=" + b.getBusId()); // ✅ ADDED
            } else {
                logger.warning("Bus not found for update: ID=" + b.getBusId()); // ✅ ADDED
            }

            return updated;
        } catch (Exception e) {
            logger.severe("Error in updateBus(): " + e.getMessage()); // ✅ ADDED
            e.printStackTrace();
        }

        return false; 
    }

    public List<Bus> bulkUpdateBuses(List<Bus> buses) { 
        logger.info("BusDAO.bulkUpdateBuses() called for " + buses.size() + " buses"); // ✅ ADDED

        List<Bus> failedUpdates = new ArrayList<>(); 
        // List to store buses that failed to update.

        String sql = "UPDATE bus SET bus_number=?, route_id=?, driver_id=?, status=? WHERE bus_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Bus b : buses) {
                ps.setString(1, b.getBusNumber());
                ps.setInt(2, b.getRouteId());
                ps.setInt(3, b.getDriverId());
                ps.setString(4, b.getStatus());
                ps.setInt(5, b.getBusId());

                int affected = ps.executeUpdate(); 
                // Update one bus at a time.

                if (affected == 0) {
                    failedUpdates.add(b); 
                    logger.warning("Failed to update bus: ID=" + b.getBusId()); // ✅ ADDED
                }
            }

        } catch (Exception e) {
            logger.severe("Error in bulkUpdateBuses(): " + e.getMessage()); // ✅ ADDED
            e.printStackTrace();
        }

        return failedUpdates; 
        // Return list of buses that failed to update.
    }

    public boolean deleteBus(int id) { 
        logger.info("BusDAO.deleteBus() called for ID=" + id); // ✅ ADDED

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM bus WHERE bus_id=?");
            ps.setInt(1, id);

            boolean deleted = ps.executeUpdate() > 0; 

            if (deleted) {
                logger.info("Bus deleted successfully: ID=" + id); // ✅ ADDED
            } else {
                logger.warning("Bus not found for deletion: ID=" + id); // ✅ ADDED
            }

            return deleted;
        } catch (Exception e) {
            logger.severe("Error in deleteBus(): " + e.getMessage()); // ✅ ADDED
            e.printStackTrace();
        }

        return false; 
    }

    public boolean deleteMultipleBuses(List<Integer> busIds) { 
        logger.info("BusDAO.deleteMultipleBuses() called for " + busIds.size() + " buses"); // ✅ ADDED

        String sql = "DELETE FROM bus WHERE bus_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Integer id : busIds) {
                ps.setInt(1, id);
                ps.addBatch(); 
                // Add each delete to batch.
            }

            int[] results = ps.executeBatch(); 

            for (int r : results) {
                if (r == 0) {
                    logger.warning("One of the bus deletions failed"); // ✅ ADDED
                    return false;
                }
            }

            logger.info("All buses deleted successfully"); // ✅ ADDED
            return true;

        } catch (Exception e) {
            logger.severe("Error in deleteMultipleBuses(): " + e.getMessage()); // ✅ ADDED
            e.printStackTrace();
        }

        return false; 
    }

    public boolean updateBusStatus(int busId, String status) { 
        logger.info("BusDAO.updateBusStatus() called for ID=" + busId + ", status=" + status); // ✅ ADDED

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE bus SET status=? WHERE bus_id=?");
            ps.setString(1, status);
            ps.setInt(2, busId);

            boolean updated = ps.executeUpdate() > 0; 

            if (updated) {
                logger.info("Bus status updated successfully: ID=" + busId); // ✅ ADDED
            } else {
                logger.warning("Bus not found for status update: ID=" + busId); // ✅ ADDED
            }

            return updated;
        } catch (Exception e) {
            logger.severe("Error in updateBusStatus(): " + e.getMessage()); // ✅ ADDED
            e.printStackTrace();
        }

        return false; 
    }

    public boolean assignDriverToBus(int busId, int driverId) { 
        logger.info("BusDAO.assignDriverToBus() called for busId=" + busId + ", driverId=" + driverId); // ✅ ADDED

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE bus SET driver_id=? WHERE bus_id=?");
            ps.setInt(1, driverId);
            ps.setInt(2, busId);

            boolean updated = ps.executeUpdate() > 0; 

            if (updated) {
                logger.info("Driver assigned successfully: busId=" + busId + ", driverId=" + driverId); // ✅ ADDED
            } else {
                logger.warning("Bus not found for driver assignment: busId=" + busId); // ✅ ADDED
            }

            return updated;
        } catch (Exception e) {
            logger.severe("Error in assignDriverToBus(): " + e.getMessage()); // ✅ ADDED
            e.printStackTrace();
        }

        return false; 
    }
}
