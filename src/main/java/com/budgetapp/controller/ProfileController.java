package com.budgetapp.controller;

import com.budgetapp.model.SessionManager;
import com.budgetapp.model.User;
import com.budgetapp.persistence.DatabaseManager;
import java.util.Map;

public class ProfileController extends BaseController {
    
    public boolean saveSettings(int userId, Map<String, String> settings) {
        if (!validateInput(settings)) {
            handleError("Invalid settings input.");
            return false;
        }
        
        // Simulated DB call based on Sequence Diagram 8
        User user = SessionManager.getInstance().getCurrentUser();
        if (user != null && user.getUserID() == userId) {
            // we update settings here
            return true;
        }
        return false;
    }
    
    private boolean validateInput(Map<String, String> settings) {
        return settings != null && !settings.isEmpty();
    }
}
