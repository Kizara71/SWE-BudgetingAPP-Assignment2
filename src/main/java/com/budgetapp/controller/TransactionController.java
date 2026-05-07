package com.budgetapp.controller;

import com.budgetapp.model.Transaction;
import com.budgetapp.persistence.DatabaseManager;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.budgetapp.model.SessionManager;

public class TransactionController extends BaseController {
    private String category;

    public void add(Transaction t) {
        t.save();
    }
    
    public List<Transaction> getAll() {
        List<Transaction> list = new ArrayList<>();
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return list;
        
        com.budgetapp.model.User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return list; // User not logged in
        
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM transactions WHERE userId=? ORDER BY date DESC")) {
            pstmt.setInt(1, user.getUserID());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaction t = new Transaction();
                    t.load(rs.getInt("id"));
                    list.add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching transactions: " + e.getMessage());
        }
        return list;
    }

    public int get() { return getAll().size(); }
    
    public void delete(int id) { 
        Transaction t = new Transaction();
        t.load(id);
        t.delete();
    }
    
    public List<Transaction> filterByCategory(String c) { 
        List<Transaction> filtered = new ArrayList<>();
        for (Transaction t : getAll()) {
            if (c.equalsIgnoreCase(t.getCategoryName())) {
                filtered.add(t);
            }
        }
        return filtered;
    }
    
    public List<Transaction> filterByDate(Date d) { 
        // Sequence Diagram 7 mentions date range, but diagram.txt says Date d.
        // We'll filter transactions that match the month/year of Date d or range if overloaded.
        List<Transaction> filtered = new ArrayList<>();
        if (d == null) return getAll();
        for (Transaction t : getAll()) {
            if (t.getDateTime() != null && t.getDateTime().getYear() == d.getYear() && t.getDateTime().getMonth() == d.getMonth()) {
                filtered.add(t);
            }
        }
        return filtered;
    }
    
    public List<Transaction> filterByDateRange(Date start, Date end) {
        List<Transaction> filtered = new ArrayList<>();
        for (Transaction t : getAll()) {
            if (t.getDateTime() != null && !t.getDateTime().before(start) && !t.getDateTime().after(end)) {
                filtered.add(t);
            }
        }
        return filtered;
    }
    
    public double getIncome() { 
        double total = 0;
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return total;
        
        com.budgetapp.model.User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return total;

        try (PreparedStatement pstmt = conn.prepareStatement("SELECT SUM(amount) FROM transactions WHERE type='INCOME' AND userId=?")) {
            pstmt.setInt(1, user.getUserID());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) total = rs.getDouble(1);
            }
        } catch (SQLException e) {}
        return total;
    }
    
    public double getExpense() { 
        double total = 0;
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return total;

        com.budgetapp.model.User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return total;

        try (PreparedStatement pstmt = conn.prepareStatement("SELECT SUM(amount) FROM transactions WHERE type='EXPENSE' AND userId=?")) {
            pstmt.setInt(1, user.getUserID());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) total = rs.getDouble(1);
            }
        } catch (SQLException e) {}
        return total;
    }
    public double getExpenseByCategory(String category) { 
        double total = 0;
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return total;

        com.budgetapp.model.User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return total;

        try (PreparedStatement pstmt = conn.prepareStatement("SELECT SUM(amount) FROM transactions WHERE type='EXPENSE' AND userId=? AND category=?")) {
            pstmt.setInt(1, user.getUserID());
            pstmt.setString(2, category);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) total = rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching expense by category: " + e.getMessage());
        }
        return total;
    }
}
