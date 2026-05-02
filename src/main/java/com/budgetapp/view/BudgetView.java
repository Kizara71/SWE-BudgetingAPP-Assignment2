package com.budgetapp.view;

import com.budgetapp.model.Budget;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BudgetView extends JPanel {
    private JComboBox<String> cmbCategory;
    private JTextField txtAmount;
    private JSpinner spinAlertThreshold;
    private JButton btnSaveBudget;
    private JPanel progressPanel;

    public BudgetView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Budgets");
        lblTitle.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // Form Panel to add a budget
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Set New Budget Limit"));

        formPanel.add(new JLabel("Category:"));
        cmbCategory = new JComboBox<>(new String[]{"Food", "Transport", "Entertainment", "Utilities", "Other"});
        formPanel.add(cmbCategory);

        formPanel.add(new JLabel("Budget Amount:"));
        txtAmount = new JTextField();
        formPanel.add(txtAmount);

        formPanel.add(new JLabel("Alert Threshold (%):"));
        spinAlertThreshold = new JSpinner(new SpinnerNumberModel(80, 1, 100, 5));
        formPanel.add(spinAlertThreshold);

        btnSaveBudget = new JButton("Save Budget");
        formPanel.add(new JLabel(""));
        formPanel.add(btnSaveBudget);

        // Progress Panel to display budgets
        progressPanel = new JPanel();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(progressPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Budgets"));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    public void updateBudgetsDisplay(List<Budget> budgets) {
        progressPanel.removeAll();
        for (Budget b : budgets) {
            JPanel itemPanel = new JPanel(new BorderLayout(5, 5));
            itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            String labelText = String.format("%s: $%.2f / $%.2f", b.getCategoryName(), b.getSpentAmount(), b.getBudgetAmount());
            JLabel lblDetails = new JLabel(labelText);
            itemPanel.add(lblDetails, BorderLayout.NORTH);

            JProgressBar progressBar = new JProgressBar(0, 100);
            int percentage = (int) b.calcSpentPercentage();
            progressBar.setValue(Math.min(percentage, 100));
            progressBar.setStringPainted(true);
            
            if (b.isOverLimit()) {
                progressBar.setForeground(Color.RED);
                lblDetails.setText(labelText + " (OVER LIMIT)");
                lblDetails.setForeground(Color.RED);
            } else if (b.checkThresholds()) {
                progressBar.setForeground(Color.ORANGE);
                lblDetails.setText(labelText + " (WARNING)");
                lblDetails.setForeground(Color.ORANGE);
            } else {
                progressBar.setForeground(new Color(0, 150, 0)); // Green
            }
            
            itemPanel.add(progressBar, BorderLayout.CENTER);
            progressPanel.add(itemPanel);
        }
        progressPanel.revalidate();
        progressPanel.repaint();
    }

    public void addSaveBudgetListener(java.awt.event.ActionListener l) {
        btnSaveBudget.addActionListener(l);
    }

    public String getCategory() { return (String) cmbCategory.getSelectedItem(); }
    public double getAmount() { 
        try {
            return Double.parseDouble(txtAmount.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    public int getAlertThreshold() { return (Integer) spinAlertThreshold.getValue(); }

    public void clearFields() {
        txtAmount.setText("");
        cmbCategory.setSelectedIndex(0);
        spinAlertThreshold.setValue(80);
    }
}
