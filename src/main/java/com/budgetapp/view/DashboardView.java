package com.budgetapp.view;

import javax.swing.*;
import java.util.List;
import java.awt.event.ActionListener;

public class DashboardView extends JPanel implements IDashboardView {
    private JLabel lblTotalBalance;
    private JTable tblRecentTransactions;
    private JButton btnAddNewTransaction;

    public DashboardView() {
        lblTotalBalance = new JLabel();
        tblRecentTransactions = new JTable();
        btnAddNewTransaction = new JButton();
    }

    @Override
    public void setBalance(double balance) {}

    @Override
    public void updateTransactionTable(List<?> data) {}

    @Override
    public void addTransactionListener(ActionListener l) {}
}
