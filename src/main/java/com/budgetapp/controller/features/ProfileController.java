package com.budgetapp.controller.features;

import com.budgetapp.model.session.SessionManager;
import com.budgetapp.model.entity.User;
import com.budgetapp.persistence.DatabaseManager;
import java.util.Map;

import com.budgetapp.controller.core.BaseController;

/**
 * Controller responsible for managing user profile settings and preferences.
 */
public class ProfileController extends BaseController {
    
    /**
     * Saves user settings and preferences to the database.
     * 
     * @param userId the ID of the user whose settings are being updated
     * @param settings a map of setting keys to values
     * @return true if settings were saved successfully, false otherwise
     */
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
    
    /**
     * Validates that the provided settings map is not null or empty.
     * 
     * @param settings the settings map to validate
     * @return true if valid, false if invalid
     */
    private boolean validateInput(Map<String, String> settings) {
        return settings != null && !settings.isEmpty();
    }
}
