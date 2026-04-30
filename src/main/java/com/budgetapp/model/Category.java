package com.budgetapp.model;

public class Category implements IPersistable {
    private int categoryID;
    private int userID;
    private String name;
    private boolean isCustom;

    public boolean canDelete() { return false; }
    public boolean isCustom() { return isCustom; }
    public int getTransactionCount() { return 0; }

    @Override
    public void save() {}

    @Override
    public void load(int id) {}

    @Override
    public boolean delete() { return false; }
}
