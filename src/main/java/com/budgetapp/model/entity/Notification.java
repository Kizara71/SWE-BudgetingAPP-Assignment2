package com.budgetapp.model.entity;

import com.budgetapp.persistence.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.budgetapp.model.interfaces.IPersistable;

/**
 * Represents a user notification alert.
 * Implements {@link IPersistable} to support database operations.
 */
public class Notification implements IPersistable {
    private int notificationID;
    private int userID;
    private String type;
    private String message;
    private boolean isRead;
    private Date timestamp;

    /**
     * Marks the notification as read and saves the updated status.
     */
    public void markAsRead() { 
        this.isRead = true; 
        save();
    }
    
    /**
     * Saves or updates the notification in the database.
     */
    @Override
    public void save() {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return;

        if (this.notificationID == 0) {
            String sql = "INSERT INTO notifications(userId, type, message, isRead, timestamp) VALUES(?,?,?,?,?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, this.userID);
                pstmt.setString(2, this.type);
                pstmt.setString(3, this.message);
                pstmt.setInt(4, this.isRead ? 1 : 0);
                pstmt.setLong(5, this.timestamp != null ? this.timestamp.getTime() : System.currentTimeMillis());
                pstmt.executeUpdate();
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        this.notificationID = rs.getInt(1);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error saving notification: " + e.getMessage());
            }
        } else {
             String sql = "UPDATE notifications SET isRead=? WHERE id=?";
             try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setInt(1, this.isRead ? 1 : 0);
                 pstmt.setInt(2, this.notificationID);
                 pstmt.executeUpdate();
             } catch (SQLException e) {
                 System.err.println("Error updating notification: " + e.getMessage());
             }
        }
    }

    /**
     * Loads the notification from the database by ID.
     * 
     * @param id the unique identifier of the notification
     */
    @Override
    public void load(int id) {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return;
        
        String sql = "SELECT * FROM notifications WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    this.notificationID = rs.getInt("id");
                    this.userID = rs.getInt("userId");
                    this.type = rs.getString("type");
                    this.message = rs.getString("message");
                    this.isRead = rs.getInt("isRead") == 1;
                    this.timestamp = new Date(rs.getLong("timestamp"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading notification: " + e.getMessage());
        }
    }

    /**
     * Deletes the notification from the database.
     * 
     * @return true if successful, false otherwise
     */
    @Override
    public boolean delete() { 
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null || this.notificationID == 0) return false;

        String sql = "DELETE FROM notifications WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, this.notificationID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting notification: " + e.getMessage());
            return false;
        }
    }

    // Getters and Setters
    public int getNotificationID() { return notificationID; }
    public void setNotificationID(int notificationID) { this.notificationID = notificationID; }
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
