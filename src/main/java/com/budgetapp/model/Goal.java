package com.budgetapp.model;

import java.util.Date;

public class Goal implements IPersistable {
    private int goalID;
    private int userID;
    private String goalName;
    private double targetAmount;
    private double currentAmount;
    private Date deadline;
    private String status;

    public double calcProgress() { return 0.0; }
    public double calcMonthlySavings() { return 0.0; }
    public void addContribution(double amount) {}
    public boolean isCompleted() { return false; }
    public String checkGoalStatus() { return ""; }

    @Override
    public void save() {}

    @Override
    public void load(int id) {}

    @Override
    public boolean delete() { return false; }
}
