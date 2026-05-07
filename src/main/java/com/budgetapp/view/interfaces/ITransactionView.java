package com.budgetapp.view.interfaces;

import java.awt.event.ActionListener;

/**
 * Interface defining the contract for transaction views.
 */
public interface ITransactionView {
    /**
     * Retrieves the transaction amount entered by the user.
     * 
     * @return the amount as a double
     */
    double getAmount();
    /**
     * Retrieves the selected category for the transaction.
     * 
     * @return the category name
     */
    String getCategory();
    /**
     * Adds an action listener to the "Save" button.
     * 
     * @param l the action listener to add
     */
    void addSaveListener(ActionListener l);
    /**
     * Clears all input fields in the transaction view.
     */
    void clearFields();
}
