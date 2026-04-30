package com.budgetapp.model;

import java.util.Date;

public class Budget implements IPersistable {
    private int budgetID;
    private int userID;
    private Category category;
    private double budgetAmount;
    private double spentAmount;
    private Date startDate;
    private Date endDate;
    private int alertThreshold;

    public double calcRemaining() { return 0.0; }
    public double calcSpentPercentage() { return 0.0; }
    public String getStatus() { return ""; }
    public void addSpending(double amount) {}
    public boolean checkThresholds() { return false; }
    public boolean isOverLimit() { return false; }
    public void resetPeriod() {}

    @Override
    public void save() {}

    @Override
    public void load(int id) {}

    @Override
    public boolean delete() { return false; }
}
