package com.budgetapp.persistence;

import com.budgetapp.model.IPersistable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        connect();
        initTables();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void connect() {
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Connect to a local database file (will create if not exists)
            connection = DriverManager.getConnection("jdbc:sqlite:budgetapp.db");
            System.out.println("Connection to SQLite has been established.");
        } catch (Exception e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
        }
    }

    private void initTables() {
        if (connection == null) return;
        
        String createUserTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "email TEXT UNIQUE," +
                "passwordHash TEXT," +
                "totalBalance REAL" +
                ");";
                
        String createTransactionTable = "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER," +
                "amount REAL," +
                "category TEXT," +
                "description TEXT," +
                "date INTEGER," +
                "type TEXT," + // INCOME or EXPENSE
                "FOREIGN KEY(userId) REFERENCES users(id)" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUserTable);
            stmt.execute(createTransactionTable);
        } catch (SQLException e) {
            System.err.println("Failed to initialize tables: " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close database: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean FindUserByEmail(String email) { return false; }
    public void executeQuery(String query) {}
    public boolean save(IPersistable item) { return false; }
    public void load(IPersistable item) {}
    public boolean delete(IPersistable item) { return false; }
}
