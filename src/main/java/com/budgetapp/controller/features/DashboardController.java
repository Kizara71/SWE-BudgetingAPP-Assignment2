package com.budgetapp.controller.features;

import com.budgetapp.view.interfaces.IDashboardView;
import com.budgetapp.model.entity.Transaction;
import java.util.List;

import com.budgetapp.controller.core.BaseController;

/**
 * Controller responsible for managing the dashboard view, computing balances,
 * and loading recent transactions.
 */
public class DashboardController extends BaseController {
    private IDashboardView view;
    private TransactionController transactionController;

    /**
     * Constructs a DashboardController linked to a specific dashboard view.
     * 
     * @param view the dashboard interface to update
     */
    public DashboardController(IDashboardView view) {
        this.view = view;
        this.transactionController = new TransactionController();
    }

    /**
     * Updates the dashboard view with the latest calculated balance and transaction data.
     */
    public void updateView() {
        if (view == null) return;
        
        // 1. Calculate Balance = Income - Expense
        double income = transactionController.getIncome();
        double expense = transactionController.getExpense();
        double balance = income - expense;
        
        view.setBalance(balance);
        
        // 2. Load recent transactions for the table
        List<Transaction> transactions = transactionController.getAll();
        view.updateTransactionTable(transactions);
    }
}
