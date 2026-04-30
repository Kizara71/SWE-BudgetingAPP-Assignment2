package com.budgetapp.view;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import org.jdatepicker.impl.*;

public class TransactionView extends JPanel implements ITransactionView {
    private JTextField txtAmount;
    private JComboBox<String> cmbCategory;
    private JDatePickerImpl datePicker;
    private JTextField txtDescription;
    private JComboBox<String> cmbType;
    private JButton btnSave;

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

    public TransactionView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        formPanel.add(new JLabel("Amount:"));
        txtAmount = new JTextField();
        formPanel.add(txtAmount);
        
        formPanel.add(new JLabel("Type:"));
        cmbType = new JComboBox<>(new String[]{"EXPENSE", "INCOME"});
        formPanel.add(cmbType);

        formPanel.add(new JLabel("Category:"));
        cmbCategory = new JComboBox<>(new String[]{"Food", "Transport", "Utilities", "Salary", "Entertainment", "Other"});
        formPanel.add(cmbCategory);

        formPanel.add(new JLabel("Description:"));
        txtDescription = new JTextField();
        formPanel.add(txtDescription);

        formPanel.add(new JLabel("Date:"));
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        formPanel.add(datePicker);

        formPanel.add(new JLabel("")); // spacer
        btnSave = new JButton("Save Transaction");
        formPanel.add(btnSave);

        add(new JLabel("<html><h2>Add New Transaction</h2></html>", SwingConstants.CENTER), BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
    }

    @Override
    public double getAmount() { 
        try { return Double.parseDouble(txtAmount.getText()); } 
        catch (NumberFormatException e) { return 0.0; }
    }

    @Override
    public String getCategory() { return (String) cmbCategory.getSelectedItem(); }
    public String getDescription() { return txtDescription.getText(); }
    public String getType() { return (String) cmbType.getSelectedItem(); }
    public Date getDate() { return (Date) datePicker.getModel().getValue(); }

    @Override
    public void addSaveListener(ActionListener l) { btnSave.addActionListener(l); }

    @Override
    public void clearFields() {
        txtAmount.setText("");
        txtDescription.setText("");
        cmbCategory.setSelectedIndex(0);
        cmbType.setSelectedIndex(0);
        datePicker.getModel().setValue(null);
    }
}
