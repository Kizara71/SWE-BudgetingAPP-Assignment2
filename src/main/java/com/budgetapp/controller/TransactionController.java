package com.budgetapp.controller;

import com.budgetapp.model.Transaction;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class TransactionController extends BaseController {
    private String category;

    public void add(Transaction t) {}
    public int get() { return 0; }
    public void delete() {}
    public List<Transaction> filterByCategory(String c) { return new ArrayList<>(); }
    public List<Transaction> filterByDate(Date d) { return new ArrayList<>(); }
    public int getIncome() { return 0; }
    public int getExpense() { return 0; }
}
