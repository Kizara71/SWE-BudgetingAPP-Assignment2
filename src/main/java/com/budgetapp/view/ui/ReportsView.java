package com.budgetapp.view.ui;

import com.budgetapp.controller.features.TransactionController;
import com.budgetapp.model.entity.Transaction;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UI View class for ReportsView.
 */
public class ReportsView extends JPanel {
    private JSpinner spinStartDate;
    private JSpinner spinEndDate;
    private JButton btnGenerate;
    private JPanel chartArea;
    private JLabel lblInsight;
    private TransactionController tc;

    /**
     * Handles ReportsView functionality.
     */
    public ReportsView() {
        tc = new TransactionController();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Start Date:"));
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date startOfMonth = cal.getTime();
        
        spinStartDate = new JSpinner(new SpinnerDateModel(startOfMonth, null, null, Calendar.DAY_OF_MONTH));
        spinStartDate.setEditor(new JSpinner.DateEditor(spinStartDate, "yyyy-MM-dd"));
        filterPanel.add(spinStartDate);

        filterPanel.add(new JLabel("End Date:"));
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endOfMonth = cal.getTime();
        
        spinEndDate = new JSpinner(new SpinnerDateModel(endOfMonth, null, null, Calendar.DAY_OF_MONTH));
        spinEndDate.setEditor(new JSpinner.DateEditor(spinEndDate, "yyyy-MM-dd"));
        filterPanel.add(spinEndDate);

        btnGenerate = new JButton("Generate Report");
        filterPanel.add(btnGenerate);

        add(filterPanel, BorderLayout.NORTH);

        // Center charts area
        chartArea = new JPanel();
        chartArea.setLayout(new BoxLayout(chartArea, BoxLayout.Y_AXIS));
        add(new JScrollPane(chartArea), BorderLayout.CENTER);

        // Bottom insight
        lblInsight = new JLabel("Select a date range to generate insights.");
        lblInsight.setFont(new Font("Inter", Font.ITALIC, 14));
        add(lblInsight, BorderLayout.SOUTH);

        btnGenerate.addActionListener(e -> generateReport());
    }

    /**
     * Handles generateReport functionality.
     */
    private void generateReport() {
        Date start = (Date) spinStartDate.getValue();
        Date end = (Date) spinEndDate.getValue();

        List<Transaction> transactions = tc.filterByDateRange(start, end);
        
        chartArea.removeAll();

        if (transactions.isEmpty()) {
            lblInsight.setText("No transaction data available for this period.");
        } else {
            double totalIncome = 0;
            double totalExpense = 0;
            Map<String, Double> expenseByCategory = new HashMap<>();

            for (Transaction t : transactions) {
                if (t.isIncome()) {
                    totalIncome += t.getAmount();
                } else {
                    totalExpense += t.getAmount();
                    expenseByCategory.put(t.getCategoryName(), 
                        expenseByCategory.getOrDefault(t.getCategoryName(), 0.0) + t.getAmount());
                }
            }

            // Summary text
            JPanel summaryPanel = new JPanel(new GridLayout(3, 1));
            summaryPanel.add(new JLabel("Total Income: $" + String.format("%.2f", totalIncome)));
            summaryPanel.add(new JLabel("Total Expense: $" + String.format("%.2f", totalExpense)));
            summaryPanel.add(new JLabel("Net Balance: $" + String.format("%.2f", totalIncome - totalExpense)));
            chartArea.add(summaryPanel);
            
            chartArea.add(Box.createRigidArea(new Dimension(0, 20)));

            // Simple text-based bar chart (Income vs Expense)
            chartArea.add(new JLabel("=== Income vs Expenses ==="));
            double maxAmount = Math.max(totalIncome, totalExpense);
            if (maxAmount > 0) {
                int incomeBars = (int) ((totalIncome / maxAmount) * 40);
                int expenseBars = (int) ((totalExpense / maxAmount) * 40);
                chartArea.add(new JLabel("INCOME:  " + "█".repeat(incomeBars)));
                chartArea.add(new JLabel("EXPENSE: " + "█".repeat(expenseBars)));
            }

            chartArea.add(Box.createRigidArea(new Dimension(0, 20)));

            // Pie chart representation (Expenses by category)
            chartArea.add(new JLabel("=== Expenses by Category ==="));
            if (totalExpense > 0) {
                for (Map.Entry<String, Double> entry : expenseByCategory.entrySet()) {
                    double pct = (entry.getValue() / totalExpense) * 100;
                    int bars = (int) (pct / 2.5); // max 40 bars for 100%
                    chartArea.add(new JLabel(String.format("%-15s [%5.1f%%]: %s", entry.getKey(), pct, "█".repeat(bars))));
                }
                
                // Key Insight
                String topCategory = "";
                double topAmount = 0;
                for (Map.Entry<String, Double> entry : expenseByCategory.entrySet()) {
                    if (entry.getValue() > topAmount) {
                        topAmount = entry.getValue();
                        topCategory = entry.getKey();
                    }
                }
                lblInsight.setText("Key Insight: Your highest expense is " + topCategory + " ($" + String.format("%.2f", topAmount) + ").");
            } else {
                lblInsight.setText("Key Insight: Great job! You have no expenses in this period.");
            }
        }
        
        chartArea.revalidate();
        chartArea.repaint();
    }
    
    /**
     * Handles updateView functionality.
     */
    public void updateView() {
        // called when tab is switched
    }
}
