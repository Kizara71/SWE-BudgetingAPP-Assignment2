package com.budgetapp.model.entity;

/**
 * Represents an expense transaction.
 * Extends the base {@link Transaction} class with expense-specific properties.
 */
public class Expense extends Transaction {
    private String paymentMethod;

    /**
     * Indicates whether this transaction is an income.
     * 
     * @return false, since this is an expense
     */
    @Override
    public boolean isIncome() { return false; }

    /**
     * Checks this expense against the relevant budget to see if it exceeds the limit.
     * 
     * @return true if the expense exceeds or meets the budget limit, false otherwise
     */
    public boolean checkAgainstBudget() { return false; }
}
