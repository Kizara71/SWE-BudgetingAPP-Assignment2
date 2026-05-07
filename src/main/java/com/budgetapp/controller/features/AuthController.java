package com.budgetapp.controller.features;

import com.budgetapp.model.entity.User;
import com.budgetapp.model.session.SessionManager;

import com.budgetapp.controller.core.BaseController;

/**
 * Controller responsible for user authentication actions like login, registration, and logout.
 */
public class AuthController extends BaseController {
    private String authToken;
    private boolean failedAttempts;

    /**
     * Registers a new user with the given credentials.
     * 
     * @param name the user's full name
     * @param email the user's email address
     * @param password the user's chosen password
     * @return true if registration is successful, false if email already exists
     */
    public boolean register(String name, String email, String password) {
        if (User.findByEmail(email) != null) {
            return false; // Email already exists
        }
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPasswordHash(password); // Simple storage for now
        newUser.save();
        return true;
    }

    /**
     * Authenticates a user with their email and password.
     * Sets the active session if successful.
     * 
     * @param email the user's email address
     * @param password the user's password
     * @return true if login is successful, false otherwise
     */
    public boolean login(String email, String password) {
        User user = User.findByEmail(email);
        if (user != null && user.validatePassword(password)) {
            SessionManager.getInstance().setCurrentUser(user);
            return true;
        }
        return false;
    }

    /**
     * Logs out the currently authenticated user by clearing the session.
     */
    public void logout() {
        SessionManager.getInstance().setCurrentUser(null);
    }

    /**
     * Performs a generic validation check.
     * 
     * @return false by default
     */
    public boolean validation() { return false; }
}
