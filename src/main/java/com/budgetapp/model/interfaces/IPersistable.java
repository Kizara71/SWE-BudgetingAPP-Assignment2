package com.budgetapp.model.interfaces;

/**
 * Interface for entities that can be persisted to the database.
 * Provides standard CRUD operation signatures.
 */
public interface IPersistable {
    /**
     * Saves the entity to the database.
     * If the entity is new, it inserts a new record; otherwise, it updates the existing record.
     */
    void save();
    /**
     * Loads the entity's data from the database.
     * 
     * @param id the unique identifier of the entity to load
     */
    void load(int id);
    /**
     * Deletes the entity from the database.
     * 
     * @return true if the deletion was successful, false otherwise
     */
    boolean delete();
}
