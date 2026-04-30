package com.budgetapp.model;

public class User implements IPersistable {
    private int userID;
    private String name;
    private String email;
    private String passwordHash;
    private double totalBalance;
    private String preferredCurrency;
    private String appLanguage;

    public void updateBalance(double amount, boolean isIncome) {}
    public boolean validatePassword(String rawPassword) { return false; }
    public boolean updateProfile(String name, String email) { return false; }

    @Override
    public void save() {}

    @Override
    public void load(int id) {}

    @Override
    public boolean delete() { return false; }
}
