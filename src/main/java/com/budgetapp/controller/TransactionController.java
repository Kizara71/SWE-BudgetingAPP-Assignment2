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

    public int get() { return 0; }
    public void delete() {}
    public List<Transaction> filterByCategory(String c) { return new ArrayList<>(); }
    public List<Transaction> filterByDate(Date d) { return new ArrayList<>(); }
    
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
}
