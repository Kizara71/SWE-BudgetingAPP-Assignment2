package com.budgetapp.model.entity;

import com.budgetapp.persistence.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.budgetapp.model.interfaces.IPersistable;

/**
 * Represents a financial or savings goal.
 * Implements {@link IPersistable} to support database operations.
 */
public class Goal implements IPersistable {
    private int goalID;
    private int userID;
    private String goalName;
    private double targetAmount;
    private double currentAmount;
    private Date deadline;
    private String status; // "IN_PROGRESS", "COMPLETED", "FAILED"

    /**
     * Calculates the progress towards the goal as a percentage.
     * 
     * @return the percentage progress, or 0 if target is 0
     */
    public double calcProgress() { 
        if (targetAmount == 0) return 0;
        return (currentAmount / targetAmount) * 100; 
    }
    
    /**
     * Calculates the required monthly savings to reach the goal by the deadline.
     * 
     * @return the monthly amount needed to be saved
     */
    public double calcMonthlySavings() { 
        if (deadline == null) return 0;
        long diffInMillies = deadline.getTime() - System.currentTimeMillis();
        long diffInMonths = diffInMillies / (1000L * 60 * 60 * 24 * 30);
        if (diffInMonths <= 0) return targetAmount - currentAmount;
        return (targetAmount - currentAmount) / diffInMonths; 
    }
    
    /**
     * Adds a contribution amount to the current progress of the goal.
     * 
     * @param amount the monetary amount to add to the goal
     */
    public void addContribution(double amount) {
        this.currentAmount += amount;
        if (isCompleted()) {
            this.status = "COMPLETED";
        }
        save();
    }
    
    /**
     * Checks if the goal has been successfully completed.
     * 
     * @return true if current amount meets or exceeds target amount
     */
    public boolean isCompleted() { return this.currentAmount >= this.targetAmount; }
    /**
     * Checks the status of the goal.
     * 
     * @return the goal status ("IN_PROGRESS", "COMPLETED", "FAILED")
     */
    public String checkGoalStatus() { return this.status; }

    /**
     * Saves or updates the goal in the database.
     */
    @Override
    public void save() {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return;

        if (this.goalID == 0) {
            String sql = "INSERT INTO goals(userId, goalName, targetAmount, currentAmount, deadline, status) VALUES(?,?,?,?,?,?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, this.userID);
                pstmt.setString(2, this.goalName);
                pstmt.setDouble(3, this.targetAmount);
                pstmt.setDouble(4, this.currentAmount);
                pstmt.setLong(5, this.deadline != null ? this.deadline.getTime() : 0);
                pstmt.setString(6, this.status != null ? this.status : "IN_PROGRESS");
                pstmt.executeUpdate();
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        this.goalID = rs.getInt(1);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error saving goal: " + e.getMessage());
            }
        } else {
             String sql = "UPDATE goals SET goalName=?, targetAmount=?, currentAmount=?, deadline=?, status=? WHERE id=?";
             try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setString(1, this.goalName);
                 pstmt.setDouble(2, this.targetAmount);
                 pstmt.setDouble(3, this.currentAmount);
                 pstmt.setLong(4, this.deadline != null ? this.deadline.getTime() : 0);
                 pstmt.setString(5, this.status);
                 pstmt.setInt(6, this.goalID);
                 pstmt.executeUpdate();
             } catch (SQLException e) {
                 System.err.println("Error updating goal: " + e.getMessage());
             }
        }
    }

    /**
     * Loads the goal from the database by ID.
     * 
     * @param id the unique identifier of the goal
     */
    @Override
    public void load(int id) {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return;
        
        String sql = "SELECT * FROM goals WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    this.goalID = rs.getInt("id");
                    this.userID = rs.getInt("userId");
                    this.goalName = rs.getString("goalName");
                    this.targetAmount = rs.getDouble("targetAmount");
                    this.currentAmount = rs.getDouble("currentAmount");
                    this.deadline = new Date(rs.getLong("deadline"));
                    this.status = rs.getString("status");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading goal: " + e.getMessage());
        }
    }

    /**
     * Deletes the goal from the database.
     * 
     * @return true if successful, false otherwise
     */
    @Override
    public boolean delete() { 
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null || this.goalID == 0) return false;

        String sql = "DELETE FROM goals WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, this.goalID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting goal: " + e.getMessage());
            return false;
        }
    }

    // Getters and Setters
    public int getGoalID() { return goalID; }
    public void setGoalID(int goalID) { this.goalID = goalID; }
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    public String getGoalName() { return goalName; }
    public void setGoalName(String goalName) { this.goalName = goalName; }
    public double getTargetAmount() { return targetAmount; }
    public void setTargetAmount(double targetAmount) { this.targetAmount = targetAmount; }
    public double getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(double currentAmount) { this.currentAmount = currentAmount; }
    public Date getDeadline() { return deadline; }
    public void setDeadline(Date deadline) { this.deadline = deadline; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
