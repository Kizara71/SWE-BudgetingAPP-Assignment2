package com.budgetapp.model;

public class Expense extends Transaction {
    private String paymentMethod;

    @Override
    public boolean isIncome() { return false; }

    public boolean checkAgainstBudget() { return false; }
}
