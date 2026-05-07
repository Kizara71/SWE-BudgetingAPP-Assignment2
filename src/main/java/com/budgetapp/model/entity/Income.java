package com.budgetapp.model.entity;

/**
 * Represents an income transaction.
 * Extends the base {@link Transaction} class with income-specific properties.
 */
public class Income extends Transaction {
    private String source;

    /**
     * Indicates whether this transaction is an income.
     * 
     * @return true, since this is an income
     */
    @Override
    public boolean isIncome() { return true; }

    /**
     * Applies this income amount to a specific savings or financial goal.
     * 
     * @param goalID the unique identifier of the goal to apply the income to
     */
    public void applyToGoal(int goalID) {}
}
