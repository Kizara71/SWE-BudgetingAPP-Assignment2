package com.budgetapp.persistence;

import com.budgetapp.model.IPersistable;

public class DatabaseManager {
    private static DatabaseManager instance;

    private DatabaseManager() {}

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public boolean FindUserByEmail(String email) { return false; }
    public void connect() {}
    public void disconnect() {}
    public void executeQuery(String query) {}
    public boolean save(IPersistable item) { return false; }
    public void load(IPersistable item) {}
    public boolean delete(IPersistable item) { return false; }
}
