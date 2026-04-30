package com.budgetapp.view;

public interface IBudgetView {
    void updateProgress(String category, double percent);
    void showWarning(String msg);
}
