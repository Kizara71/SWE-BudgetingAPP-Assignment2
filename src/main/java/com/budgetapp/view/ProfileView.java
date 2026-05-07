package com.budgetapp.view;

import com.budgetapp.controller.ProfileController;
import com.budgetapp.model.SessionManager;
import com.budgetapp.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ProfileView extends JPanel {
    private JLabel lblName;
    private JLabel lblEmail;
    private JLabel lblBalance;
    private JComboBox<String> cmbCurrency;
    private JComboBox<String> cmbLanguage;
    private JComboBox<String> cmbAppearance;
    private JButton btnSave;
    private ProfileController controller;

    public ProfileView() {
        controller = new ProfileController();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        lblName = new JLabel("Name: ");
        lblEmail = new JLabel("Email: ");
        lblBalance = new JLabel("Total Balance: ");
        headerPanel.add(lblName);
        headerPanel.add(lblEmail);
        headerPanel.add(lblBalance);

        JPanel settingsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Settings"));

        settingsPanel.add(new JLabel("Currency:"));
        cmbCurrency = new JComboBox<>(new String[]{"USD"});
        settingsPanel.add(cmbCurrency);

        settingsPanel.add(new JLabel("Language:"));
        cmbLanguage = new JComboBox<>(new String[]{"English"});
        settingsPanel.add(cmbLanguage);

        settingsPanel.add(new JLabel("Appearance:"));
        cmbAppearance = new JComboBox<>(new String[]{"Light", "Dark"});
        settingsPanel.add(cmbAppearance);

        btnSave = new JButton("Save Settings");
        settingsPanel.add(new JLabel("")); // spacer
        settingsPanel.add(btnSave);

        add(headerPanel, BorderLayout.NORTH);
        add(settingsPanel, BorderLayout.CENTER);

        btnSave.addActionListener(e -> saveProfile());
    }

    public void updateView() {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user != null) {
            lblName.setText("Name: " + user.getName());
            lblEmail.setText("Email: " + user.getEmail());
            lblBalance.setText("Total Balance: $" + String.format("%.2f", user.getTotalBalance()));
        }
    }

    private void saveProfile() {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return;

        Map<String, String> settings = new HashMap<>();
        settings.put("currency", (String) cmbCurrency.getSelectedItem());
        settings.put("language", (String) cmbLanguage.getSelectedItem());
        settings.put("appearance", (String) cmbAppearance.getSelectedItem());

        boolean success = controller.saveSettings(user.getUserID(), settings);
        if (success) {
            try {
                if ("Dark".equals(settings.get("appearance"))) {
                    UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
                } else {
                    UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
                }
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window != null) {
                    SwingUtilities.updateComponentTreeUI(window);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Profile Settings Saved Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, controller.getErrorMSG() != null ? controller.getErrorMSG() : "Failed to save settings.", "Error", JOptionPane.ERROR_MESSAGE);
            controller.clearError();
        }
    }
}
