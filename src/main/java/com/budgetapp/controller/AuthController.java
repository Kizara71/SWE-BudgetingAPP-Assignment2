package com.budgetapp.controller;

import com.budgetapp.model.User;
import com.budgetapp.model.SessionManager;

public class AuthController extends BaseController {
    private String authToken;
    private boolean failedAttempts;

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

    public boolean login(String email, String password) {
        User user = User.findByEmail(email);
        if (user != null && user.validatePassword(password)) {
            SessionManager.getInstance().setCurrentUser(user);
            return true;
        }
        return false;
    }

    public void logout() {
        SessionManager.getInstance().setCurrentUser(null);
    }

    public boolean validation() { return false; }
}
