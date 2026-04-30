package com.budgetapp.view;

import com.budgetapp.model.Transaction;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionListener;

public class DashboardView extends JPanel implements IDashboardView {
    private JLabel lblTotalBalance;
    private JTable tblRecentTransactions;
    private DefaultTableModel tableModel;
    private JButton btnRefresh;

    public DashboardView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel for balance
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblTotalBalance = new JLabel("Total Balance: $0.00");
        lblTotalBalance.setFont(new Font("Arial", Font.BOLD, 24));
        btnRefresh = new JButton("Refresh");
        topPanel.add(lblTotalBalance);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(btnRefresh);
        add(topPanel, BorderLayout.NORTH);

        // Center panel for table
        String[] columns = {"ID", "Date", "Category", "Description", "Type", "Amount"};
        tableModel = new DefaultTableModel(columns, 0);
        tblRecentTransactions = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblRecentTransactions);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void setBalance(double balance) {
        lblTotalBalance.setText(String.format("Total Balance: $%.2f", balance));
    }

    @Override
    public void updateTransactionTable(List<?> data) {
        tableModel.setRowCount(0); // clear existing
        for (Object obj : data) {
            if (obj instanceof Transaction) {
                Transaction t = (Transaction) obj;
                tableModel.addRow(new Object[]{
                    t.getTransactionID(),
                    t.getFormattedDate(),
                    t.getCategoryName(),
                    t.getDescription(),
                    t.getType(),
                    String.format("$%.2f", t.getAmount())
                });
            }
        }
    }

    @Override
    public void addTransactionListener(ActionListener l) {
        btnRefresh.addActionListener(l);
    }
}
