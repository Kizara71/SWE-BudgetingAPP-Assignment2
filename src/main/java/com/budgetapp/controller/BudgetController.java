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

    public void delete(int id) {
        Budget b = new Budget();
        b.load(id);
        b.delete();
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
