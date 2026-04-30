package com.budgetapp.model;

public class Income extends Transaction {
    private String source;

    @Override
    public boolean isIncome() { return true; }

    public void applyToGoal(int goalID) {}
}
