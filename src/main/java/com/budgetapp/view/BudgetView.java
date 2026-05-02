package com.budgetapp.view;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;

public class BudgetView extends JPanel implements IBudgetView {
    private Map<String, JProgressBar> progressBars;
    private JButton btnSetLimit;

    public BudgetView() {
        progressBars = new HashMap<>();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("Budgets");
        lblTitle.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel();
        centerPanel.add(new JLabel("Budget tracking features coming soon..."));
        add(centerPanel, BorderLayout.CENTER);
        
        btnSetLimit = new JButton("Set Limits");
        add(btnSetLimit, BorderLayout.SOUTH);
    }

    @Override
    public void updateProgress(String category, double percent) {}

    @Override
    public void showWarning(String msg) {}
}
