package com.budgetapp.model;

import java.util.Date;

public class Transaction implements IPersistable {
    private int transactionID;
    private int userID;
    private double amount;
    private Category category;
    private String description;
    private Date dateTime;
    private String paymentMethod;

    public boolean isIncome() { return false; }
    public boolean validateAmount() { return false; }
    public String getFormattedDate() { return ""; }

    @Override
    public void save() {}

    @Override
    public void load(int id) {}

    @Override
    public boolean delete() { return false; }
}
