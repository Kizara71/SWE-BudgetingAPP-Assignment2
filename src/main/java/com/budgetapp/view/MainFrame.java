package com.budgetapp.view;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
    private static MainFrame instance;
    private DashboardView dashboardPanel;
    private TransactionView transactionPanel;
    private BudgetView budgetPanel;

    private MainFrame() {
        dashboardPanel = new DashboardView();
        transactionPanel = new TransactionView();
        budgetPanel = new BudgetView();
    }

    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }

    public void switchPanel(String panelName) {}
}
