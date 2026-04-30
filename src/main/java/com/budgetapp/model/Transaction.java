package com.budgetapp.model;

import com.budgetapp.persistence.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Transaction implements IPersistable {
    private int transactionID;
    private int userID;
    private double amount;
    private Category category;
    private String description;
    private Date dateTime;
    private String paymentMethod;
    private String type; // "INCOME" or "EXPENSE"
    private String categoryName;

    public boolean isIncome() { return "INCOME".equalsIgnoreCase(this.type); }
    public boolean validateAmount() { return this.amount > 0; }
    public String getFormattedDate() { 
        if (dateTime == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(dateTime); 
    }

    @Override
    public void save() {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return;

        if (this.transactionID == 0) {
            String sql = "INSERT INTO transactions(userId, amount, category, description, date, type) VALUES(?,?,?,?,?,?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, this.userID);
                pstmt.setDouble(2, this.amount);
                pstmt.setString(3, this.category != null ? this.category.getName() : this.categoryName);
                pstmt.setString(4, this.description);
                pstmt.setLong(5, this.dateTime != null ? this.dateTime.getTime() : System.currentTimeMillis());
                pstmt.setString(6, this.type);
                pstmt.executeUpdate();
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        this.transactionID = rs.getInt(1);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error saving transaction: " + e.getMessage());
            }
        } else {
             String sql = "UPDATE transactions SET amount=?, category=?, description=?, date=?, type=? WHERE id=?";
             try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setDouble(1, this.amount);
                 pstmt.setString(2, this.category != null ? this.category.getName() : this.categoryName);
                 pstmt.setString(3, this.description);
                 pstmt.setLong(4, this.dateTime != null ? this.dateTime.getTime() : System.currentTimeMillis());
                 pstmt.setString(5, this.type);
                 pstmt.setInt(6, this.transactionID);
                 pstmt.executeUpdate();
             } catch (SQLException e) {
                 System.err.println("Error updating transaction: " + e.getMessage());
             }
        }
    }

    @Override
    public void load(int id) {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) return;
        
        String sql = "SELECT * FROM transactions WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    this.transactionID = rs.getInt("id");
                    this.userID = rs.getInt("userId");
                    this.amount = rs.getDouble("amount");
                    this.categoryName = rs.getString("category");
                    this.description = rs.getString("description");
                    this.dateTime = new Date(rs.getLong("date"));
                    this.type = rs.getString("type");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading transaction: " + e.getMessage());
        }
    }

    @Override
    public boolean delete() {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null || this.transactionID == 0) return false;

        String sql = "DELETE FROM transactions WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, this.transactionID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting transaction: " + e.getMessage());
            return false;
        }
    }

    public int getTransactionID() { return transactionID; }
    public void setUserID(int userID) { this.userID = userID; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public void setType(String type) { this.type = type; }
    public String getCategoryName() { return categoryName; }
    public void setDateTime(Date dateTime) { this.dateTime = dateTime; }
    public double getAmount() { return amount; }
    public Date getDateTime() { return dateTime; }
    public String getType() { return type; }
    public void setDescription(String description) { this.description = description; }
    public String getDescription() { return description; }
}
