package com.budgetapp.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static MainFrame instance;
    private DashboardView dashboardPanel;
    private TransactionView transactionPanel;
    private BudgetView budgetPanel;
    private GoalView goalPanel;
    private ReportsView reportsPanel;
    private ProfileView profilePanel;
    private JPanel rootPanel;
    private CardLayout rootCardLayout;
    private JPanel appContainer;
    private AuthView authView;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    private MainFrame() {
        setTitle("Budgeting App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        rootCardLayout = new CardLayout();
        rootPanel = new JPanel(rootCardLayout);

        authView = new AuthView();
        
        // Setup App Container (Sidebar + Content)
        appContainer = new JPanel(new BorderLayout());
        setupAppContainer();

        rootPanel.add(authView, "Auth");
        rootPanel.add(appContainer, "App");

        getContentPane().add(rootPanel);
        rootCardLayout.show(rootPanel, "Auth");
    }

    private void setupAppContainer() {
        // Sidebar for navigation
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(150, 600));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnDashboard = new JButton("Dashboard");
        JButton btnTransaction = new JButton("Transactions");
        JButton btnBudget = new JButton("Budgets");
        JButton btnGoal = new JButton("Goals");
        JButton btnReports = new JButton("Reports");
        JButton btnProfile = new JButton("Profile");
        JButton btnLogout = new JButton("Logout");

        // Make buttons stretch
        btnDashboard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnTransaction.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnBudget.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnGoal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnReports.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnProfile.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnLogout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        sidebar.add(btnDashboard);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnBudget);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnGoal);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnReports);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnTransaction);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnProfile);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnLogout);

        // Content area with CardLayout
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);

        dashboardPanel = new DashboardView();
        transactionPanel = new TransactionView();
        budgetPanel = new BudgetView();
        goalPanel = new GoalView();
        reportsPanel = new ReportsView();
        profilePanel = new ProfileView();

        mainContentPanel.add(dashboardPanel, "Dashboard");
        mainContentPanel.add(transactionPanel, "Transaction");
        mainContentPanel.add(budgetPanel, "Budget");
        mainContentPanel.add(goalPanel, "Goal");
        mainContentPanel.add(reportsPanel, "Reports");
        mainContentPanel.add(profilePanel, "Profile");

        // Controller Wiring
        com.budgetapp.controller.DashboardController dashController = new com.budgetapp.controller.DashboardController(dashboardPanel);
        com.budgetapp.controller.TransactionController transController = new com.budgetapp.controller.TransactionController();
        com.budgetapp.controller.BudgetController budgetController = new com.budgetapp.controller.BudgetController();

        budgetPanel.addSaveBudgetListener(e -> {
            com.budgetapp.model.Budget b = new com.budgetapp.model.Budget();
            b.setCategoryName(budgetPanel.getCategory());
            b.setBudgetAmount(budgetPanel.getAmount());
            b.setAlertThreshold(budgetPanel.getAlertThreshold());
            b.setStartDate(budgetPanel.getStartDate());
            b.setEndDate(budgetPanel.getEndDate());
            
            if (b.getBudgetAmount() <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid budget amount.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            budgetController.create(b);
            if (budgetController.getErrorMSG() != null) {
                JOptionPane.showMessageDialog(this, budgetController.getErrorMSG(), "Error", JOptionPane.ERROR_MESSAGE);
                budgetController.clearError(); // reset
                return;
            }
            budgetPanel.clearFields();
            budgetPanel.updateBudgetsDisplay(budgetController.getAllBudgets());
            JOptionPane.showMessageDialog(this, "Budget Saved Successfully!");
        });

        budgetPanel.setBudgetActionListener(new com.budgetapp.view.BudgetView.BudgetActionListener() {
            @Override
            public void onEdit(com.budgetapp.model.Budget b) {
                JPanel editPanel = new JPanel(new GridLayout(2, 2, 5, 5));
                JTextField txtEditAmount = new JTextField(String.valueOf(b.getBudgetAmount()));
                JSpinner spinEditAlert = new JSpinner(new SpinnerNumberModel(b.getAlertThreshold(), 1, 100, 5));
                
                editPanel.add(new JLabel("Amount:"));
                editPanel.add(txtEditAmount);
                editPanel.add(new JLabel("Alert Threshold (%):"));
                editPanel.add(spinEditAlert);

                int result = JOptionPane.showConfirmDialog(MainFrame.this, editPanel, 
                         "Edit Budget: " + b.getCategoryName(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        double newAmount = Double.parseDouble(txtEditAmount.getText());
                        if (newAmount > 0) {
                            b.setBudgetAmount(newAmount);
                            b.setAlertThreshold((Integer) spinEditAlert.getValue());
                            budgetController.save(b);
                            budgetPanel.updateBudgetsDisplay(budgetController.getAllBudgets());
                            JOptionPane.showMessageDialog(MainFrame.this, "Budget Updated!");
                        } else {
                            JOptionPane.showMessageDialog(MainFrame.this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Invalid amount format.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            @Override
            public void onDelete(com.budgetapp.model.Budget b) {
                int result = JOptionPane.showConfirmDialog(MainFrame.this, 
                        "Are you sure you want to delete the budget for " + b.getCategoryName() + "?", 
                        "Delete Budget", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    budgetController.delete(b.getBudgetID());
                    budgetPanel.updateBudgetsDisplay(budgetController.getAllBudgets());
                }
            }
        });

        // Setup Goal Controller and Panel Wiring
        com.budgetapp.controller.GoalController goalController = new com.budgetapp.controller.GoalController();
        goalPanel.addGoalListener(e -> {
            com.budgetapp.model.Goal newGoal = new com.budgetapp.model.Goal();
            newGoal.setGoalName(JOptionPane.showInputDialog(this, "Enter Goal Name:"));
            if(newGoal.getGoalName() == null || newGoal.getGoalName().trim().isEmpty()) return;
            try {
                newGoal.setTargetAmount(Double.parseDouble(JOptionPane.showInputDialog(this, "Enter Target Amount:")));
            } catch (Exception ex) { return; }
            newGoal.setCurrentAmount(0);
            
            goalController.create(newGoal);
            goalPanel.displayGoals(goalController.getAll());
        });

        transactionPanel.addSaveListener(e -> {
            com.budgetapp.model.Transaction t = new com.budgetapp.model.Transaction();
            // Set the logged in user ID to the transaction
            com.budgetapp.model.User currentUser = com.budgetapp.model.SessionManager.getInstance().getCurrentUser();
            if (currentUser != null) {
                t.setUserID(currentUser.getUserID());
            }
            
            t.setAmount(transactionPanel.getAmount());
            t.setCategoryName(transactionPanel.getCategory());
            t.setDescription(transactionPanel.getDescription());
            t.setType(transactionPanel.getType());
            t.setDateTime(transactionPanel.getDate());
            
            transController.add(t);
            
            if (currentUser != null) {
                currentUser.updateBalance(t.getAmount(), t.isIncome());
            }
            
            // Check for budget notifications
            if (!t.isIncome()) {
                for (com.budgetapp.model.Budget b : budgetController.getAllBudgets()) {
                    if (b.getCategoryName().equalsIgnoreCase(t.getCategoryName())) {
                        if (b.isOverLimit()) {
                            JOptionPane.showMessageDialog(this, "ALERT: You have exceeded your budget for " + b.getCategoryName() + "!", "Budget Exceeded", JOptionPane.WARNING_MESSAGE);
                            new com.budgetapp.controller.NotifController().send("Exceeded budget for " + b.getCategoryName(), "BUDGET_EXCEEDED", currentUser != null ? currentUser.getUserID() : 0);
                        } else if (b.checkThresholds()) {
                            JOptionPane.showMessageDialog(this, "WARNING: You are nearing your budget limit for " + b.getCategoryName() + ".", "Budget Warning", JOptionPane.WARNING_MESSAGE);
                            new com.budgetapp.controller.NotifController().send("Nearing budget limit for " + b.getCategoryName(), "BUDGET_WARNING", currentUser != null ? currentUser.getUserID() : 0);
                        }
                    }
                }
            }
            
            // Update goals based on transaction
            double amountToApply = t.getAmount();
            for (com.budgetapp.model.Goal g : goalController.getAll()) {
                if (amountToApply <= 0) break;
                
                if (t.isIncome()) {
                    if (!g.isCompleted()) {
                        double needed = g.getTargetAmount() - g.getCurrentAmount();
                        if (amountToApply <= needed) {
                            g.addContribution(amountToApply);
                            amountToApply = 0;
                        } else {
                            g.addContribution(needed);
                            amountToApply -= needed;
                        }
                    }
                } else {
                    if (g.getCurrentAmount() > 0) {
                        double available = g.getCurrentAmount();
                        if (amountToApply <= available) {
                            g.setCurrentAmount(g.getCurrentAmount() - amountToApply);
                            g.setStatus("IN_PROGRESS");
                            g.save();
                            amountToApply = 0;
                        } else {
                            g.setCurrentAmount(0);
                            g.setStatus("IN_PROGRESS");
                            g.save();
                            amountToApply -= available;
                        }
                    }
                }
            }
            
            transactionPanel.clearFields();
            JOptionPane.showMessageDialog(this, "Transaction Saved!");
            
            // refresh dashboard
            dashController.updateView();
        });
        
        dashboardPanel.addTransactionListener(e -> dashController.updateView());

        // Layout setup
        appContainer.add(sidebar, BorderLayout.WEST);
        appContainer.add(mainContentPanel, BorderLayout.CENTER);

        // Navigation Actions
        btnDashboard.addActionListener(e -> { switchPanel("Dashboard"); dashController.updateView(); });
        btnTransaction.addActionListener(e -> switchPanel("Transaction"));
        btnBudget.addActionListener(e -> { 
            switchPanel("Budget"); 
            budgetPanel.updateBudgetsDisplay(budgetController.getAllBudgets()); 
        });
        btnGoal.addActionListener(e -> {
            switchPanel("Goal");
            goalPanel.displayGoals(goalController.getAll());
        });
        btnReports.addActionListener(e -> {
            switchPanel("Reports");
            reportsPanel.updateView();
        });
        btnProfile.addActionListener(e -> {
            switchPanel("Profile");
            profilePanel.updateView();
        });
        
        btnLogout.addActionListener(e -> {
            new com.budgetapp.controller.AuthController().logout();
            rootCardLayout.show(rootPanel, "Auth");
        });
    }

    public void onLoginSuccess() {
        rootCardLayout.show(rootPanel, "App");
        // refresh data for newly logged in user
        com.budgetapp.controller.DashboardController dashController = new com.budgetapp.controller.DashboardController(dashboardPanel);
        dashController.updateView();
    }

    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }

    public void switchPanel(String panelName) {
        cardLayout.show(mainContentPanel, panelName);
    }
    
    public DashboardView getDashboardPanel() { return dashboardPanel; }
    public TransactionView getTransactionPanel() { return transactionPanel; }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            // Global font update
            UIManager.put("defaultFont", new Font("Inter", Font.PLAIN, 14));
            // High arc for mobile-like rounded corners
            UIManager.put("Button.arc", 20);
            UIManager.put("Component.arc", 20);
            UIManager.put("ProgressBar.arc", 20);
            UIManager.put("TextComponent.arc", 20);
            
            // Light theme global overrides
            UIManager.put("Panel.background", Color.decode("#F3F4F6")); // Light gray app background
            UIManager.put("Window.background", Color.decode("#F3F4F6"));
            UIManager.put("Button.background", Color.decode("#3B82F6")); // Blue primary button
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Component.focusColor", Color.decode("#3B82F6"));
            UIManager.put("Component.borderColor", Color.decode("#D1D5DB")); // Light gray border
            UIManager.put("TabbedPane.selectedBackground", Color.WHITE);
            
            // Sidebar buttons to look more like nav items
            UIManager.put("Button.margin", new Insets(10, 15, 10, 15));
        } catch(Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        SwingUtilities.invokeLater(() -> {
            MainFrame.getInstance().setVisible(true);
        });
    }
}
