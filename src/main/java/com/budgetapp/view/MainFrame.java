package com.budgetapp.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static MainFrame instance;
    private DashboardView dashboardPanel;
    private TransactionView transactionPanel;
    private BudgetView budgetPanel;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    private MainFrame() {
        setTitle("Budgeting App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Sidebar for navigation
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(150, 600));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnDashboard = new JButton("Dashboard");
        JButton btnTransaction = new JButton("Transactions");
        JButton btnBudget = new JButton("Budgets");

        // Make buttons stretch
        btnDashboard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnTransaction.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnBudget.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        sidebar.add(btnDashboard);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnTransaction);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnBudget);

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
            t.setAmount(transactionPanel.getAmount());
            t.setCategoryName(transactionPanel.getCategory());
            t.setDescription(transactionPanel.getDescription());
            t.setType(transactionPanel.getType());
            t.setDateTime(transactionPanel.getDate());
            
            transController.add(t);
            transactionPanel.clearFields();
            JOptionPane.showMessageDialog(this, "Transaction Saved!");
            
            // refresh dashboard
            dashController.updateView();
        });
        
        dashboardPanel.addTransactionListener(e -> dashController.updateView());

        // Initial Data Load
        dashController.updateView();

        // Layout setup
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidebar, BorderLayout.WEST);
        getContentPane().add(mainContentPanel, BorderLayout.CENTER);

        // Navigation Actions
        btnDashboard.addActionListener(e -> switchPanel("Dashboard"));
        btnTransaction.addActionListener(e -> switchPanel("Transaction"));
        btnBudget.addActionListener(e -> switchPanel("Budget"));
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
        SwingUtilities.invokeLater(() -> {
            MainFrame.getInstance().setVisible(true);
        });
    }
}
