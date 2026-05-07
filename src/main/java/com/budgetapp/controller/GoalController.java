package com.budgetapp.controller;

import com.budgetapp.model.Goal;
import com.budgetapp.persistence.DatabaseManager;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class GoalController extends BaseController {
    private int progressTracker;

    public void create(Goal g) {
        g.save();
    }
    
    public void updateGoal(int id, double amount) {
        Goal g = new Goal();
        g.load(id);
        g.setTargetAmount(amount);
        g.save();
    }
    
    public void updateProgress(int id, double currentAmount, String status) {
        Goal g = new Goal();
        g.load(id);
        g.setCurrentAmount(currentAmount);
        g.setStatus(status);
        g.save();
    }
    
    public void delete(int id) {
        Goal g = new Goal();
        g.load(id);
        g.delete();
    }
    
    public String getGoal() { 
        List<Goal> goals = getAll();
        if (goals.isEmpty()) return "";
        return goals.get(0).getGoalName(); 
    }
    
    public float getProgress() { 
        List<Goal> goals = getAll();
        if (goals.isEmpty()) return 0.0f;
        return (float) goals.get(0).calcProgress(); 
    }
    
    public boolean checkCompletion() { 
        List<Goal> goals = getAll();
        for (Goal g : goals) {
            if (g.isCompleted()) return true;
        }
        return false; 
    }
    
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
