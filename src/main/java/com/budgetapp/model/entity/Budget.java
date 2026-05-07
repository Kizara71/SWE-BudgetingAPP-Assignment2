package com.budgetapp.model.entity;

import com.budgetapp.persistence.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.budgetapp.model.interfaces.IPersistable;

/**
 * Represents a budget set for a specific category.
 * Implements {@link IPersistable} to support database operations.
 */
public class Budget implements IPersistable {
    private int budgetID;
    private int userID;
    private String categoryName;
    private double budgetAmount;
    private double spentAmount;
    private Date startDate;
    private Date endDate;
    private int alertThreshold;

    public Budget() {}

    public int getBudgetID() { return budgetID; }
    public void setBudgetID(int budgetID) { this.budgetID = budgetID; }
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public double getBudgetAmount() { return budgetAmount; }
    public void setBudgetAmount(double budgetAmount) { this.budgetAmount = budgetAmount; }
    public double getSpentAmount() { return spentAmount; }
    public void setSpentAmount(double spentAmount) { this.spentAmount = spentAmount; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public int getAlertThreshold() { return alertThreshold; }
    public void setAlertThreshold(int alertThreshold) { this.alertThreshold = alertThreshold; }

    /**
     * Calculates the remaining amount in the budget.
     * 
     * @return the remaining budget amount
     */
    public double calcRemaining() { 
        return budgetAmount - spentAmount; 
    }

    /**
     * Calculates the percentage of the budget that has been spent.
     * 
     * @return the percentage spent, or 0 if the budget amount is 0
     */
    public double calcSpentPercentage() { 
        if (budgetAmount == 0) return 0;
        return (spentAmount / budgetAmount) * 100; 
    }

    /**
     * Gets the current status of the budget based on spending and thresholds.
     * 
     * @return a status string: "Over Limit", "Warning", or "Normal"
     */
    public String getStatus() { 
        if (isOverLimit()) return "Over Limit";
        if (checkThresholds()) return "Warning";
        return "Normal";
    }

    /**
     * Checks if the spent percentage has reached or exceeded the alert threshold.
     * 
     * @return true if the threshold is met or exceeded, false otherwise
     */
    public boolean checkThresholds() { 
        return calcSpentPercentage() >= alertThreshold; 
    }

    /**
     * Checks if the spent amount exceeds the total budget amount.
     * 
     * @return true if spent amount is greater than the budget, false otherwise
     */
    public boolean isOverLimit() { 
        return spentAmount > budgetAmount; 
    }

    /**
     * Saves or updates the budget in the database.
     */
    @Override
    public void save() {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return;

        if (this.budgetID == 0) {
            String sql = "INSERT INTO budgets(userId, category, amount, startDate, endDate, alertThreshold) VALUES(?,?,?,?,?,?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, this.userID);
                pstmt.setString(2, this.categoryName);
                pstmt.setDouble(3, this.budgetAmount);
                pstmt.setLong(4, this.startDate != null ? this.startDate.getTime() : 0);
                pstmt.setLong(5, this.endDate != null ? this.endDate.getTime() : 0);
                pstmt.setInt(6, this.alertThreshold);
                pstmt.executeUpdate();
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        this.budgetID = rs.getInt(1);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error saving budget: " + e.getMessage());
            }
        } else {
             String sql = "UPDATE budgets SET category=?, amount=?, startDate=?, endDate=?, alertThreshold=? WHERE id=?";
             try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setString(1, this.categoryName);
                 pstmt.setDouble(2, this.budgetAmount);
                 pstmt.setLong(3, this.startDate != null ? this.startDate.getTime() : 0);
                 pstmt.setLong(4, this.endDate != null ? this.endDate.getTime() : 0);
                 pstmt.setInt(5, this.alertThreshold);
                 pstmt.setInt(6, this.budgetID);
                 pstmt.executeUpdate();
             } catch (SQLException e) {
                 System.err.println("Error updating budget: " + e.getMessage());
             }
        }
    }

    /**
     * Loads the budget from the database using the given ID.
     * 
     * @param id the unique identifier of the budget to load
     */
    @Override
    public void load(int id) {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return;
        
        String sql = "SELECT * FROM budgets WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    this.budgetID = rs.getInt("id");
                    this.userID = rs.getInt("userId");
                    this.categoryName = rs.getString("category");
                    this.budgetAmount = rs.getDouble("amount");
                    this.startDate = new Date(rs.getLong("startDate"));
                    this.endDate = new Date(rs.getLong("endDate"));
                    this.alertThreshold = rs.getInt("alertThreshold");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading budget: " + e.getMessage());
        }
    }

    /**
     * Deletes the budget from the database.
     * 
     * @return true if the deletion was successful, false otherwise
     */
    @Override
    public boolean delete() { 
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null || this.budgetID == 0) return false;

        String sql = "DELETE FROM budgets WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, this.budgetID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting budget: " + e.getMessage());
            return false;
        }
    }
}
