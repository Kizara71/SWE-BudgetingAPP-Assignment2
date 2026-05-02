package com.budgetapp.model;

import com.budgetapp.persistence.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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

    public double calcRemaining() { 
        return budgetAmount - spentAmount; 
    }

    public double calcSpentPercentage() { 
        if (budgetAmount == 0) return 0;
        return (spentAmount / budgetAmount) * 100; 
    }

    public String getStatus() { 
        if (isOverLimit()) return "Over Limit";
        if (checkThresholds()) return "Warning";
        return "Normal";
    }

    public boolean checkThresholds() { 
        return calcSpentPercentage() >= alertThreshold; 
    }

    public boolean isOverLimit() { 
        return spentAmount > budgetAmount; 
    }

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
