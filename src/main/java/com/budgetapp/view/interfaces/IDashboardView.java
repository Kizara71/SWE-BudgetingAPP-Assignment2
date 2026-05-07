package com.budgetapp.view.interfaces;

import java.util.List;
import java.awt.event.ActionListener;

/**
 * Interface defining the contract for dashboard views.
 */
public interface IDashboardView {
    /**
     * Updates the displayed total balance on the dashboard.
     * 
     * @param balance the new balance amount
     */
    void setBalance(double balance);
    /**
     * Updates the transaction table with a new list of transactions.
     * 
     * @param data a list of transaction objects to display
     */
    void updateTransactionTable(List<?> data);
    /**
     * Adds an action listener to the "Add Transaction" button.
     * 
     * @param l the action listener to add
     */
    void addTransactionListener(ActionListener l);
}
