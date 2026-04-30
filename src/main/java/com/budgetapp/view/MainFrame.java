package com.budgetapp.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static MainFrame instance;
    private DashboardView dashboardPanel;
    private TransactionView transactionPanel;
    private BudgetView budgetPanel;
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
        JButton btnLogout = new JButton("Logout");

        // Make buttons stretch
        btnDashboard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnTransaction.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnBudget.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnLogout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        sidebar.add(btnDashboard);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnTransaction);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnBudget);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnLogout);

        // Content area with CardLayout
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);

        dashboardPanel = new DashboardView();
        transactionPanel = new TransactionView();
        budgetPanel = new BudgetView();

        mainContentPanel.add(dashboardPanel, "Dashboard");
        mainContentPanel.add(transactionPanel, "Transaction");
        mainContentPanel.add(budgetPanel, "Budget");

        // Controller Wiring
        com.budgetapp.controller.DashboardController dashController = new com.budgetapp.controller.DashboardController(dashboardPanel);
        com.budgetapp.controller.TransactionController transController = new com.budgetapp.controller.TransactionController();

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
        btnBudget.addActionListener(e -> switchPanel("Budget"));
        
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
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
            // Global font update
            UIManager.put("defaultFont", new Font("Inter", Font.PLAIN, 14));
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("ProgressBar.arc", 10);
            UIManager.put("TextComponent.arc", 10);
        } catch(Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        SwingUtilities.invokeLater(() -> {
            MainFrame.getInstance().setVisible(true);
        });
    }
}
