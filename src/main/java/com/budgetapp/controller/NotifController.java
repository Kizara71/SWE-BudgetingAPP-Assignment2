package com.budgetapp.controller;

import com.budgetapp.model.Notification;
import com.budgetapp.persistence.DatabaseManager;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.sql.*;

public class NotifController extends BaseController {
    private int unreadCount;
    private List<Notification> notifList;
    private Date lastSent;

    public String send(String msg, String type, int userId) {
        Notification n = new Notification();
        n.setUserID(userId);
        n.setMessage(msg);
        n.setType(type);
        n.setRead(false);
        n.save();
        return "Sent";
    }

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

    public void markAsRead(int id) {
        Notification n = new Notification();
        n.load(id);
        n.markAsRead();
    }

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

    public void clear(int userId) {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return;
        try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM notifications WHERE userId=?")) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {}
    }
}
