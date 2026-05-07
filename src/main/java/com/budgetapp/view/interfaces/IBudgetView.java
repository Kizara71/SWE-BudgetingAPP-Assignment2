package com.budgetapp.view.interfaces;

/**
 * Interface defining the contract for budget views.
 */
public interface IBudgetView {
    /**
     * Updates the progress bar or visual indicator for a specific category.
     * 
     * @param category the name of the budget category
     * @param percent the percentage of the budget spent
     */
    void updateProgress(String category, double percent);
    /**
     * Displays a warning message to the user, typically when approaching or exceeding a limit.
     * 
     * @param msg the warning message to display
     */
    void showWarning(String msg);
}
