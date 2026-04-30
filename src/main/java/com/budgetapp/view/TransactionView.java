package com.budgetapp.view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class TransactionView extends JPanel implements ITransactionView {
    private JTextField txtAmount;
    private JComboBox<String> cmbCategory;
    // JDatePicker placeholder
    private JComponent datePicker;
    private JButton btnSave;

    public TransactionView() {
        txtAmount = new JTextField();
        cmbCategory = new JComboBox<>();
        btnSave = new JButton();
    }

    @Override
    public double getAmount() { return 0.0; }

    @Override
    public String getCategory() { return ""; }

    @Override
    public void addSaveListener(ActionListener l) {}

    @Override
    public void clearFields() {}
}
