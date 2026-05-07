package com.budgetapp.controller.features;

import com.budgetapp.model.entity.Notification;
import com.budgetapp.persistence.DatabaseManager;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.sql.*;

import com.budgetapp.controller.core.BaseController;

/**
 * Controller responsible for managing user notifications and alerts.
 */
public class NotifController extends BaseController {
    private int unreadCount;
    private List<Notification> notifList;
    private Date lastSent;

    /**
     * Creates and sends a new notification to a specific user.
     * 
     * @param msg the notification message
     * @param type the type of notification (e.g. BUDGET_WARNING)
     * @param userId the ID of the user receiving the notification
     * @return a status string indicating the result
     */
    public String send(String msg, String type, int userId) {
        Notification n = new Notification();
        n.setUserID(userId);
        n.setMessage(msg);
        n.setType(type);
        n.setRead(false);
        n.save();
        return "Sent";
    }

    /**
     * Retrieves all notifications for a specific user, ordered by timestamp descending.
     * 
     * @param userId the user ID to fetch notifications for
     * @return a list of notifications
     */
    public List<Notification> getAll(int userId) {
        List<Notification> list = new ArrayList<>();
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return list;
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM notifications WHERE userId=? ORDER BY timestamp DESC")) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Notification n = new Notification();
                    n.load(rs.getInt("id"));
                    list.add(n);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching notifications: " + e.getMessage());
        }
        return list;
    }

    /**
     * Marks a specific notification as read.
     * 
     * @param id the ID of the notification
     */
    public void markAsRead(int id) {
        Notification n = new Notification();
        n.load(id);
        n.markAsRead();
    }

    /**
     * Gets the total count of unread notifications for a user.
     * 
     * @param userId the ID of the user
     * @return the number of unread notifications
     */
    public int getUnreadCount(int userId) {
        int count = 0;
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return 0;
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM notifications WHERE userId=? AND isRead=0")) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) count = rs.getInt(1);
            }
        } catch (SQLException e) {}
        return count;
    }

    /**
     * Clears and deletes all notifications for a given user.
     * 
     * @param userId the ID of the user
     */
    public void clear(int userId) {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return;
        try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM notifications WHERE userId=?")) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {}
    }
}
