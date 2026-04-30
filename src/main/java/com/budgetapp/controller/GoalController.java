package com.budgetapp.controller;

import com.budgetapp.model.Goal;
import java.util.Date;

public class GoalController extends BaseController {
    private int progressTracker;

    public void create(Goal g) {}
    public void updateGoal(int id, double amount) {}
    public void updateProgress(double currentAmount, String status) {}
    public void delete(int id) {}
    public String getGoal() { return ""; }
    public float getProgress() { return 0.0f; }
    public boolean checkCompletion() { return false; }
    public double calcMonthlySavings(double targetAmount, Date deadline, double currentAmount) { return 0.0; }
}
