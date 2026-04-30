package com.budgetapp.model;

public interface IPersistable {
    void save();
    void load(int id);
    boolean delete();
}
