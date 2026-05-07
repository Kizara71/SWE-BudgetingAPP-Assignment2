package com.budgetapp.model.session;

import com.budgetapp.model.entity.User;

/**
 * Manages the currently logged-in user session.
 * This class follows the Singleton design pattern.
 */
public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {}

    /**
     * Retrieves the singleton instance of SessionManager.
     * 
     * @return the SessionManager instance
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Sets the currently active user for this session.
     * 
     * @param user the {@link User} to set as currently logged in
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Gets the currently active user for this session.
     * 
     * @return the currently logged-in {@link User}, or null if no user is logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }
}
