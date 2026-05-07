package com.budgetapp.model.entity;

import com.budgetapp.persistence.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.budgetapp.model.interfaces.IPersistable;

/**
 * Represents a registered user in the application.
 * Implements {@link IPersistable} to support database operations.
 */
public class User implements IPersistable {
    private int userID;
    private String name;
    private String email;
    private String passwordHash;
    private double totalBalance;
    private String preferredCurrency;
    private String appLanguage;

    /**
     * Updates the user's total balance based on a new transaction amount.
     * 
     * @param amount the transaction amount
     * @param isIncome true if adding to balance, false if deducting
     */
    public void updateBalance(double amount, boolean isIncome) {
        if (isIncome) {
            this.totalBalance += amount;
        } else {
            this.totalBalance -= amount;
        }
        save(); // persist changes
    }
    
    /**
     * Validates a provided plain-text password against the stored hash.
     * 
     * @param rawPassword the provided password
     * @return true if the passwords match, false otherwise
     */
    public boolean validatePassword(String rawPassword) { 
        return rawPassword.equals(this.passwordHash); 
    }
    
    /**
     * Updates the user's basic profile details.
     * 
     * @param name the new display name
     * @param email the new email address
     * @return always returns true
     */
    public boolean updateProfile(String name, String email) { 
        this.name = name;
        this.email = email;
        save();
        return true; 
    }

    /**
     * Saves or updates the user in the database.
     */
    @Override
    public void save() {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return;

        if (this.userID == 0) {
            String sql = "INSERT INTO users(name, email, passwordHash, totalBalance) VALUES(?,?,?,?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, this.name);
                pstmt.setString(2, this.email);
                pstmt.setString(3, this.passwordHash);
                pstmt.setDouble(4, this.totalBalance);
                pstmt.executeUpdate();
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        this.userID = rs.getInt(1);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error saving user: " + e.getMessage());
            }
        } else {
            String sql = "UPDATE users SET name=?, email=?, passwordHash=?, totalBalance=? WHERE id=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, this.name);
                pstmt.setString(2, this.email);
                pstmt.setString(3, this.passwordHash);
                pstmt.setDouble(4, this.totalBalance);
                pstmt.setInt(5, this.userID);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error updating user: " + e.getMessage());
            }
        }
    }

    /**
     * Loads the user from the database by ID.
     * 
     * @param id the unique identifier of the user
     */
    @Override
    public void load(int id) {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return;
        
        String sql = "SELECT * FROM users WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    this.userID = rs.getInt("id");
                    this.name = rs.getString("name");
                    this.email = rs.getString("email");
                    this.passwordHash = rs.getString("passwordHash");
                    this.totalBalance = rs.getDouble("totalBalance");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading user: " + e.getMessage());
        }
    }

    /**
     * Deletes the user from the database.
     * 
     * @return true if successful, false otherwise
     */
    @Override
    public boolean delete() { 
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null || this.userID == 0) return false;

        String sql = "DELETE FROM users WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, this.userID);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
    
    public int getUserID() { return userID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public double getTotalBalance() { return totalBalance; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setTotalBalance(double balance) { this.totalBalance = balance; }

    /**
     * Utility method to find a user in the database by their email address.
     * 
     * @param email the email to search for
     * @return the {@link User} object if found, or null if not found
     */
    public static User findByEmail(String email) {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return null;
        
        String sql = "SELECT * FROM users WHERE email=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.userID = rs.getInt("id");
                    user.name = rs.getString("name");
                    user.email = rs.getString("email");
                    user.passwordHash = rs.getString("passwordHash");
                    user.totalBalance = rs.getDouble("totalBalance");
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by email: " + e.getMessage());
        }
        return null;
    }
}
