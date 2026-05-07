package com.budgetapp.controller.features;

import com.budgetapp.model.entity.Budget;
import com.budgetapp.persistence.DatabaseManager;
import com.budgetapp.model.session.SessionManager;
import com.budgetapp.model.entity.User;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.budgetapp.controller.core.BaseController;

/**
 * Controller responsible for managing budgets.
 * Handles creating, updating, and validating budget limits for categories.
 */
public class BudgetController extends BaseController {

    /**
     * Saves a budget to the database for the current logged-in user.
     * 
     * @param b the budget entity to save
     */
    public void save(Budget b) {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user != null) {
            b.setUserID(user.getUserID());
            b.save();
        }
    }
    
    /**
     * Creates a new budget. Checks for existing duplicates by category name before saving.
     * 
     * @param b the budget entity to create
     */
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
    
    /**
     * Updates an existing budget with new information.
     * 
     * @param id the ID of the budget to edit
     * @param b the budget entity with updated information
     */
    public void edit(int id, Budget b) {
        b.setBudgetID(id);
        save(b);
    }

    /**
     * Deletes a budget by its ID.
     * 
     * @param id the ID of the budget to delete
     */
    public void delete(int id) {
        Budget b = new Budget();
        b.load(id);
        b.delete();
    }
    
    /**
     * Gets the first budget amount.
     * 
     * @return the budget amount or 0 if no budgets exist
     */
    public double getBudget() {
        List<Budget> budgets = getAllBudgets();
        if (budgets.isEmpty()) return 0;
        return budgets.get(0).getBudgetAmount();
    }
    
    /**
     * Gets the remaining amount of the first budget.
     * 
     * @return the remaining amount or 0 if no budgets exist
     */
    public double getRemaining() {
        List<Budget> budgets = getAllBudgets();
        if (budgets.isEmpty()) return 0;
        return budgets.get(0).calcRemaining();
    }
    
    /**
     * Checks if any of the budgets have reached their warning threshold or over limit.
     * 
     * @return true if an alert status is found
     */
    public boolean checkStatus() {
        List<Budget> budgets = getAllBudgets();
        for (Budget b : budgets) {
            if (b.checkThresholds() || b.isOverLimit()) return true;
        }
        return false;
    }
    
    /**
     * Triggers a notification alert if the specified budget is over its limit.
     * 
     * @param id the ID of the budget to check
     */
    public void triggerAlert(int id) {
        Budget b = new Budget();
        b.load(id);
        if (b.isOverLimit()) {
            User user = SessionManager.getInstance().getCurrentUser();
            new NotifController().send("Exceeded budget for " + b.getCategoryName(), "BUDGET_EXCEEDED", user != null ? user.getUserID() : 0);
        }
    }
    
    /**
     * Calculates and updates the spent amount for a specific budget based on recent transactions.
     * 
     * @param id the ID of the budget
     */
    public void calcSpent(int id) {
        Budget b = new Budget();
        b.load(id);
        TransactionController transController = new TransactionController();
        double spent = transController.getExpenseByCategory(b.getCategoryName());
        b.setSpentAmount(spent);
        save(b);
    }

    /**
     * Retrieves all budgets associated with the current user, dynamically calculating spent amounts.
     * 
     * @return a list of budgets
     */
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
