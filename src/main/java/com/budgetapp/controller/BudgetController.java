package com.budgetapp.controller;

import com.budgetapp.model.Budget;
import com.budgetapp.persistence.DatabaseManager;
import com.budgetapp.model.SessionManager;
import com.budgetapp.model.User;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BudgetController extends BaseController {

    public void save(Budget b) {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user != null) {
            b.setUserID(user.getUserID());
            b.save();
        }
    }
    
    public void create(Budget b) {
        // Duplicate check as per Sequence Diagram 4
        List<Budget> existing = getAllBudgets();
        for (Budget existingBudget : existing) {
            if (existingBudget.getCategoryName().equalsIgnoreCase(b.getCategoryName())) {
                // If the month/year overlaps or is the same, it's a duplicate.
                // Simple duplicate check on category for now.
                handleError("Please edit the existing budget instead");
                return;
            }
        }
        save(b);
    }
    
    public void edit(int id, Budget b) {
        b.setBudgetID(id);
        save(b);
    }

    public void delete(int id) {
        Budget b = new Budget();
        b.load(id);
        b.delete();
    }
    
    public double getBudget() {
        List<Budget> budgets = getAllBudgets();
        if (budgets.isEmpty()) return 0;
        return budgets.get(0).getBudgetAmount();
    }
    
    public double getRemaining() {
        List<Budget> budgets = getAllBudgets();
        if (budgets.isEmpty()) return 0;
        return budgets.get(0).calcRemaining();
    }
    
    public boolean checkStatus() {
        List<Budget> budgets = getAllBudgets();
        for (Budget b : budgets) {
            if (b.checkThresholds() || b.isOverLimit()) return true;
        }
        return false;
    }
    
    public void triggerAlert(int id) {
        Budget b = new Budget();
        b.load(id);
        if (b.isOverLimit()) {
            User user = SessionManager.getInstance().getCurrentUser();
            new NotifController().send("Exceeded budget for " + b.getCategoryName(), "BUDGET_EXCEEDED", user != null ? user.getUserID() : 0);
        }
    }
    
    public void calcSpent(int id) {
        Budget b = new Budget();
        b.load(id);
        TransactionController transController = new TransactionController();
        double spent = transController.getExpenseByCategory(b.getCategoryName());
        b.setSpentAmount(spent);
        save(b);
    }

    public List<Budget> getAllBudgets() {
        List<Budget> budgets = new ArrayList<>();
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return budgets;
        
        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return budgets;

        TransactionController transController = new TransactionController();

        try (PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM budgets WHERE userId=?")) {
            pstmt.setInt(1, user.getUserID());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Budget b = new Budget();
                    b.load(rs.getInt("id"));
                    // calculate spent amount dynamically
                    double spent = transController.getExpenseByCategory(b.getCategoryName());
                    b.setSpentAmount(spent);
                    budgets.add(b);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching budgets: " + e.getMessage());
        }
        return budgets;
    }
}
