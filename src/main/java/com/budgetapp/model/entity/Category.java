package com.budgetapp.model.entity;

import com.budgetapp.model.interfaces.IPersistable;

/**
 * Represents a budget category that transactions can be assigned to.
 * Implements {@link IPersistable} for database operations.
 */
public class Category implements IPersistable {
    private int categoryID;
    private int userID;
    private String name;
    private boolean isCustom;

    /**
     * Checks if this category can be deleted.
     * Custom categories or categories without transactions can typically be deleted.
     * 
     * @return true if the category can be deleted, false otherwise
     */
    public boolean canDelete() { return false; }
    /**
     * Determines whether this category was created by the user (custom) or is a default category.
     * 
     * @return true if it is a custom category, false if default
     */
    public boolean isCustom() { return isCustom; }
    /**
     * Gets the total number of transactions associated with this category.
     * 
     * @return the number of transactions
     */
    public int getTransactionCount() { return 0; }
    /**
     * Gets the name of the category.
     * 
     * @return the category name
     */
    public String getName() { return name; }

    /**
     * Saves the current state of the category to the database.
     */
    @Override
    public void save() {}

    /**
     * Loads the category from the database using its ID.
     * 
     * @param id the unique identifier of the category
     */
    @Override
    public void load(int id) {}

    /**
     * Deletes the category from the database.
     * 
     * @return true if deletion was successful, false otherwise
     */
    @Override
    public boolean delete() { return false; }
}
