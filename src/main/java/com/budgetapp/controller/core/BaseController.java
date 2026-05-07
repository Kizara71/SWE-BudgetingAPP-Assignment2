package com.budgetapp.controller.core;

/**
 * Abstract base class for all controllers in the application.
 * Provides common functionality such as authentication checking and error handling.
 */
public abstract class BaseController {
    protected int currentUserID;
    protected boolean isAuthenticated;
    protected String errorMSG;
    protected int sessionTimeout;

    /**
     * Gets the ID of the currently authenticated user.
     * 
     * @return the user ID
     */
    public int getCurrentUserID() { return currentUserID; }
    
    /**
     * Checks if a user is currently authenticated.
     * 
     * @return true if authenticated, false otherwise
     */
    public boolean checkAuth() { return isAuthenticated; }
    
    /**
     * Sets an error message to be handled or displayed.
     * 
     * @param msg the error message
     */
    public void handleError(String msg) { this.errorMSG = msg; }
    
    /**
     * Gets the current error message.
     * 
     * @return the error message, or null if none
     */
    public String getErrorMSG() { return errorMSG; }
    
    /**
     * Clears the current error message.
     */
    public void clearError() { this.errorMSG = null; }
}
