package com.budgetapp.controller.features;

import com.budgetapp.model.entity.Transaction;
import com.budgetapp.persistence.DatabaseManager;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.budgetapp.model.session.SessionManager;

import com.budgetapp.controller.core.BaseController;

/**
 * Controller responsible for managing transactions (income and expenses).
 * Handles CRUD operations, filtering, and balance calculations.
 */
public class TransactionController extends BaseController {
    private String category;

    /**
     * Adds a new transaction to the database.
     * 
     * @param t the transaction to add
     */
    public void add(Transaction t) {
        t.save();
    }
    
    /**
     * Retrieves all transactions associated with the current user, ordered by date.
     * 
     * @return a list of transactions
     */
    public List<Transaction> getAll() {
        List<Transaction> list = new ArrayList<>();
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return list;
        
        com.budgetapp.model.entity.User user = SessionManager.getInstance().getCurrentUser();
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

    /**
     * Gets the total number of transactions for the current user.
     * 
     * @return the number of transactions
     */
    public int get() { return getAll().size(); }
    
    /**
     * Deletes a transaction by its ID.
     * 
     * @param id the ID of the transaction to delete
     */
    public void delete(int id) { 
        Transaction t = new Transaction();
        t.load(id);
        t.delete();
    }
    
    /**
     * Filters transactions by a specific category name.
     * 
     * @param c the category name to filter by
     * @return a list of matching transactions
     */
    public List<Transaction> filterByCategory(String c) { 
        List<Transaction> filtered = new ArrayList<>();
        for (Transaction t : getAll()) {
            if (c.equalsIgnoreCase(t.getCategoryName())) {
                filtered.add(t);
            }
        }
        return filtered;
    }
    
    /**
     * Filters transactions by month and year of the specified date.
     * 
     * @param d the target date
     * @return a list of transactions occurring in that month and year
     */
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
    
    /**
     * Filters transactions within a specific date range.
     * 
     * @param start the start date (inclusive)
     * @param end the end date (inclusive)
     * @return a list of transactions in the specified range
     */
    public List<Transaction> filterByDateRange(Date start, Date end) {
        List<Transaction> filtered = new ArrayList<>();
        for (Transaction t : getAll()) {
            if (t.getDateTime() != null && !t.getDateTime().before(start) && !t.getDateTime().after(end)) {
                filtered.add(t);
            }
        }
        return filtered;
    }
    
    /**
     * Calculates the total income for the current user.
     * 
     * @return the total accumulated income amount
     */
    public double getIncome() { 
        double total = 0;
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return total;
        
        com.budgetapp.model.entity.User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return total;

        try (PreparedStatement pstmt = conn.prepareStatement("SELECT SUM(amount) FROM transactions WHERE type='INCOME' AND userId=?")) {
            pstmt.setInt(1, user.getUserID());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) total = rs.getDouble(1);
            }
        } catch (SQLException e) {}
        return total;
    }
    
    /**
     * Calculates the total expenses for the current user.
     * 
     * @return the total accumulated expense amount
     */
    public double getExpense() { 
        double total = 0;
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return total;

        com.budgetapp.model.entity.User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return total;

        try (PreparedStatement pstmt = conn.prepareStatement("SELECT SUM(amount) FROM transactions WHERE type='EXPENSE' AND userId=?")) {
            pstmt.setInt(1, user.getUserID());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) total = rs.getDouble(1);
            }
        } catch (SQLException e) {}
        return total;
    }
    /**
     * Calculates the total expenses within a specific category for the current user.
     * 
     * @param category the category name to filter by
     * @return the total expense amount for that category
     */
    public double getExpenseByCategory(String category) { 
        double total = 0;
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return total;

        com.budgetapp.model.entity.User user = SessionManager.getInstance().getCurrentUser();
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
