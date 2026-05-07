package com.budgetapp.controller.features;

import com.budgetapp.model.entity.Goal;
import com.budgetapp.persistence.DatabaseManager;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

import com.budgetapp.controller.core.BaseController;

/**
 * Controller responsible for managing user financial goals.
 * Handles CRUD operations and progress tracking for goals.
 */
public class GoalController extends BaseController {
    private int progressTracker;

    /**
     * Creates and saves a new financial goal.
     * 
     * @param g the goal entity to create
     */
    public void create(Goal g) {
        g.save();
    }
    
    /**
     * Updates the target amount for an existing goal.
     * 
     * @param id the ID of the goal to update
     * @param amount the new target amount
     */
    public void updateGoal(int id, double amount) {
        Goal g = new Goal();
        g.load(id);
        g.setTargetAmount(amount);
        g.save();
    }
    
    /**
     * Updates the current progress and status of a goal.
     * 
     * @param id the ID of the goal
     * @param currentAmount the updated current amount
     * @param status the new status string
     */
    public void updateProgress(int id, double currentAmount, String status) {
        Goal g = new Goal();
        g.load(id);
        g.setCurrentAmount(currentAmount);
        g.setStatus(status);
        g.save();
    }
    
    /**
     * Deletes a goal by its ID.
     * 
     * @param id the ID of the goal
     */
    public void delete(int id) {
        Goal g = new Goal();
        g.load(id);
        g.delete();
    }
    
    /**
     * Gets the name of the most urgent goal.
     * 
     * @return the goal name
     */
    public String getGoal() { 
        List<Goal> goals = getAll();
        if (goals.isEmpty()) return "";
        return goals.get(0).getGoalName(); 
    }
    
    /**
     * Gets the progress percentage of the most urgent goal.
     * 
     * @return the progress percentage
     */
    public float getProgress() { 
        List<Goal> goals = getAll();
        if (goals.isEmpty()) return 0.0f;
        return (float) goals.get(0).calcProgress(); 
    }
    
    /**
     * Checks if any goal in the database is fully completed.
     * 
     * @return true if at least one goal is completed
     */
    public boolean checkCompletion() { 
        List<Goal> goals = getAll();
        for (Goal g : goals) {
            if (g.isCompleted()) return true;
        }
        return false; 
    }
    
    /**
     * Calculates the required monthly savings to achieve a goal by its deadline.
     * 
     * @param targetAmount the target savings amount
     * @param deadline the target completion date
     * @param currentAmount the current saved amount
     * @return the recommended monthly savings amount
     */
    public double calcMonthlySavings(double targetAmount, Date deadline, double currentAmount) { 
        if (deadline == null) return 0;
        long diffInMillies = deadline.getTime() - System.currentTimeMillis();
        long diffInMonths = diffInMillies / (1000L * 60 * 60 * 24 * 30);
        if (diffInMonths <= 0) return targetAmount - currentAmount;
        return (targetAmount - currentAmount) / diffInMonths; 
    }

    public List<Goal> getAll() {
        List<Goal> list = new ArrayList<>();
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return list;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id FROM goals ORDER BY deadline ASC")) {
            while (rs.next()) {
                Goal g = new Goal();
                g.load(rs.getInt("id"));
                list.add(g);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching goals: " + e.getMessage());
        }
        return list;
    }
}
