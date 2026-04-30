package com.budgetapp.controller;

import com.budgetapp.model.Budget;

public class BudgetController extends BaseController {
    private int spentAmount;
    private int alertThreshold;

    private void calcSpent(int id) {}
    public void create(Budget b) {}
    public void edit(int id, Budget b) {}
    public int getBudget() { return 0; }
    public int getRemaining() { return 0; }
    public boolean checkStatus() { return false; }
    public void triggerAlert(int id) {}
}
