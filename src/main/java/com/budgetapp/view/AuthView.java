package com.budgetapp.view;

import com.budgetapp.controller.AuthController;
import javax.swing.*;
import java.awt.*;

public class AuthView extends JPanel {
    private AuthController authController;
    private CardLayout cardLayout;
    private JPanel cardsPanel;
    
    // Login fields
    private JTextField loginEmailField;
    private JPasswordField loginPasswordField;
    
    // Register fields
    private JTextField regNameField;
    private JTextField regEmailField;
    private JPasswordField regPasswordField;

    public AuthView() {
        authController = new AuthController();
        setLayout(new BorderLayout());
        setBackground(Color.decode("#1A233A"));

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.setBackground(Color.decode("#1A233A"));
        
        cardsPanel.add(createLoginPanel(), "Login");
        cardsPanel.add(createRegisterPanel(), "Register");
        
        add(cardsPanel, BorderLayout.CENTER);
    }
    
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panel.setBackground(Color.decode("#1A233A"));
        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Inter", Font.BOLD, 28));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Color.WHITE);
        gbc.gridy = 1; panel.add(lblEmail, gbc);
        gbc.gridx = 1; loginEmailField = new JTextField(15); panel.add(loginEmailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; 
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(Color.WHITE);
        panel.add(lblPassword, gbc);
        gbc.gridx = 1; loginPasswordField = new JPasswordField(15); panel.add(loginPasswordField, gbc);

        JButton loginBtn = new JButton("Log In");
        loginBtn.putClientProperty("JButton.buttonType", "roundRect");
        loginBtn.setBackground(Color.decode("#3B82F6"));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Inter", Font.BOLD, 14));
        loginBtn.addActionListener(e -> {
            String email = loginEmailField.getText();
            String password = new String(loginPasswordField.getPassword());
            if (authController.login(email, password)) {
                MainFrame.getInstance().onLoginSuccess();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton goToRegBtn = new JButton("Don't have an account? Sign Up");
        goToRegBtn.putClientProperty("JButton.buttonType", "borderless");
        goToRegBtn.setForeground(Color.decode("#94A3B8"));
        goToRegBtn.addActionListener(e -> cardLayout.show(cardsPanel, "Register"));

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(loginBtn, gbc);
        gbc.gridy = 4;
        panel.add(goToRegBtn, gbc);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panel.setBackground(Color.decode("#1A233A"));
        JLabel title = new JLabel("Create Account", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Inter", Font.BOLD, 28));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        JLabel lblRegName = new JLabel("Full Name:");
        lblRegName.setForeground(Color.WHITE);
        gbc.gridy = 1; panel.add(lblRegName, gbc);
        gbc.gridx = 1; regNameField = new JTextField(15); panel.add(regNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; 
        JLabel lblRegEmail = new JLabel("Email:");
        lblRegEmail.setForeground(Color.WHITE);
        panel.add(lblRegEmail, gbc);
        gbc.gridx = 1; regEmailField = new JTextField(15); panel.add(regEmailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; 
        JLabel lblRegPass = new JLabel("Password:");
        lblRegPass.setForeground(Color.WHITE);
        panel.add(lblRegPass, gbc);
        gbc.gridx = 1; regPasswordField = new JPasswordField(15); panel.add(regPasswordField, gbc);

        JButton regBtn = new JButton("Create Account");
        regBtn.putClientProperty("JButton.buttonType", "roundRect");
        regBtn.setBackground(Color.decode("#10B981")); // green for create account as per mockup
        regBtn.setForeground(Color.WHITE);
        regBtn.setFont(new Font("Inter", Font.BOLD, 14));
        regBtn.addActionListener(e -> {
            String name = regNameField.getText();
            String email = regEmailField.getText();
            String password = new String(regPasswordField.getPassword());
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (authController.register(name, email, password)) {
                JOptionPane.showMessageDialog(this, "Registration Successful! Please Login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(cardsPanel, "Login");
            } else {
                JOptionPane.showMessageDialog(this, "Email already exists", "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton goToLoginBtn = new JButton("Already have an account? Log In");
        goToLoginBtn.putClientProperty("JButton.buttonType", "borderless");
        goToLoginBtn.setForeground(Color.decode("#94A3B8"));
        goToLoginBtn.addActionListener(e -> cardLayout.show(cardsPanel, "Login"));

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(regBtn, gbc);
        gbc.gridy = 5;
        panel.add(goToLoginBtn, gbc);

        return panel;
    }
}
