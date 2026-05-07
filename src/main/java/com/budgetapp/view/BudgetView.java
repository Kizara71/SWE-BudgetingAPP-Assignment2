package com.budgetapp.view;

import com.budgetapp.model.Budget;
import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.jdatepicker.impl.*;

public class BudgetView extends JPanel {
    private JComboBox<String> cmbCategory;
    private JTextField txtAmount;
    private JSpinner spinAlertThreshold;
    private JDatePickerImpl startDatePicker;
    private JDatePickerImpl endDatePicker;
    private JButton btnSaveBudget;
    private JPanel progressPanel;

    class DateLabelFormatter extends AbstractFormatter {
        private String datePattern = "yyyy-MM-dd";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
        @Override
        public Object stringToValue(String text) throws ParseException { return dateFormatter.parseObject(text); }
        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) { return dateFormatter.format(((Calendar) value).getTime()); }
            return "";
        }
    }

    public interface BudgetActionListener {
        void onEdit(Budget b);

        void onDelete(Budget b);
    }

    private BudgetActionListener actionListener;

    public void setBudgetActionListener(BudgetActionListener listener) {
        this.actionListener = listener;
    }

    public BudgetView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Budgets");
        lblTitle.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // Form Panel to add a budget
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Set New Budget Limit"));

        formPanel.add(new JLabel("Category:"));
        cmbCategory = new JComboBox<>(new String[] { "Food", "Transport", "Entertainment", "Utilities", "Other" });
        cmbCategory.setEditable(true);
        formPanel.add(cmbCategory);

        formPanel.add(new JLabel("Budget Amount:"));
        txtAmount = new JTextField();
        formPanel.add(txtAmount);

        formPanel.add(new JLabel("Alert Threshold (%):"));
        spinAlertThreshold = new JSpinner(new SpinnerNumberModel(80, 1, 100, 5));
        formPanel.add(spinAlertThreshold);
        
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        formPanel.add(new JLabel("Start Date:"));
        startDatePicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(), p), new DateLabelFormatter());
        formPanel.add(startDatePicker);

        formPanel.add(new JLabel("End Date:"));
        endDatePicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(), p), new DateLabelFormatter());
        formPanel.add(endDatePicker);

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

            String labelText = String.format("%s: $%.2f / $%.2f", b.getCategoryName(), b.getSpentAmount(),
                    b.getBudgetAmount());
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
                progressBar.setForeground(Color.decode("#fca311"));
                lblDetails.setText(labelText + " (WARNING)");
                lblDetails.setForeground(Color.decode("#fca311"));
            } else {
                progressBar.setForeground(Color.decode("#3B82F6")); // Primary Blue
            }

            JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnEdit = new JButton("Edit");
            JButton btnDelete = new JButton("Delete");

            btnEdit.addActionListener(e -> {
                if (actionListener != null)
                    actionListener.onEdit(b);
            });

            btnDelete.addActionListener(e -> {
                if (actionListener != null)
                    actionListener.onDelete(b);
            });

            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);

            itemPanel.add(progressBar, BorderLayout.CENTER);
            itemPanel.add(actionPanel, BorderLayout.SOUTH);
            progressPanel.add(itemPanel);
        }
        progressPanel.revalidate();
        progressPanel.repaint();
    }

    public void addSaveBudgetListener(java.awt.event.ActionListener l) {
        btnSaveBudget.addActionListener(l);
    }

    public String getCategory() {
        return (String) cmbCategory.getSelectedItem();
    }

    public double getAmount() {
        try {
            return Double.parseDouble(txtAmount.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getAlertThreshold() {
        return (Integer) spinAlertThreshold.getValue();
    }

    public Date getStartDate() { return (Date) startDatePicker.getModel().getValue(); }
    public Date getEndDate() { return (Date) endDatePicker.getModel().getValue(); }

    public void clearFields() {
        txtAmount.setText("");
        cmbCategory.setSelectedIndex(0);
        spinAlertThreshold.setValue(80);
        startDatePicker.getModel().setValue(null);
        endDatePicker.getModel().setValue(null);
    }
}
