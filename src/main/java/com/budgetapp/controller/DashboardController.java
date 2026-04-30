package com.budgetapp.controller;

import com.budgetapp.view.IDashboardView;
import com.budgetapp.model.Transaction;
import java.util.List;

public class DashboardController extends BaseController {
    private IDashboardView view;
    private TransactionController transactionController;

    public DashboardController(IDashboardView view) {
        this.view = view;
        this.transactionController = new TransactionController();
    }

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
