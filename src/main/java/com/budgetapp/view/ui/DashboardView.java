package com.budgetapp.view.ui;
import com.budgetapp.view.interfaces.IDashboardView;

import com.budgetapp.model.entity.Transaction;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionListener;

/**
 * UI View class for DashboardView.
 */
public class DashboardView extends JPanel implements IDashboardView {
    private JLabel lblTotalBalance;
    private JTable tblRecentTransactions;
    private DefaultTableModel tableModel;

    /**
     * Handles DashboardView functionality.
     */
    public DashboardView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel for balance (Card style)
        JPanel balanceCard = new JPanel();
        balanceCard.setLayout(new BoxLayout(balanceCard, BoxLayout.Y_AXIS));
        balanceCard.setBackground(Color.decode("#3B82F6")); // primary blue
        balanceCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#3B82F6"), 1, true),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        JLabel lblTitle = new JLabel("Total Balance");
        lblTitle.setForeground(new Color(255, 255, 255, 200)); // semi-transparent white
        lblTitle.setFont(new Font("Inter", Font.PLAIN, 16));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblTotalBalance = new JLabel("$0.00");
        lblTotalBalance.setForeground(Color.WHITE);
        lblTotalBalance.setFont(new Font("Inter", Font.BOLD, 42));
        lblTotalBalance.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        balanceCard.add(lblTitle);
        balanceCard.add(Box.createVerticalStrut(10));
        balanceCard.add(lblTotalBalance);

        // Header Panel for Card + Button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(balanceCard, BorderLayout.CENTER);
        
        // No btnRefresh anymore
        
        add(headerPanel, BorderLayout.NORTH);

        // Center panel for table
        String[] columns = {"ID", "Date", "Category", "Description", "Type", "Amount"};
        tableModel = new DefaultTableModel(columns, 0);
        tblRecentTransactions = new JTable(tableModel);
        tblRecentTransactions.setRowHeight(35); // taller rows for modern look
        tblRecentTransactions.getTableHeader().setFont(new Font("Inter", Font.BOLD, 14));
        
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JLabel lblRecent = new JLabel("Recent Transactions");
        lblRecent.setFont(new Font("Inter", Font.BOLD, 18));
        lblRecent.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        tableContainer.add(lblRecent, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(tblRecentTransactions);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // remove scrollpane border
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(tableContainer, BorderLayout.CENTER);
    }

    @Override
    public void setBalance(double balance) {
        lblTotalBalance.setText(String.format("$%.2f", balance));
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
        // Obsolete, data refreshes automatically on tab switch
    }
}
