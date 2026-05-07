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
        setLayout(new GridBagLayout()); // Use GridBagLayout to center the card
        
        JPanel cardPanel = new JPanel(new BorderLayout(0, 20));
        cardPanel.setPreferredSize(new Dimension(500, 480));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor"), 1, true),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        JLabel lblTitle = new JLabel("Add New Transaction");
        lblTitle.setFont(new Font("Inter", Font.BOLD, 22));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        cardPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 15));

        formPanel.add(new JLabel("Amount:"));
        txtAmount = new JTextField();
        formPanel.add(txtAmount);
        
        formPanel.add(new JLabel("Type:"));
        cmbType = new JComboBox<>(new String[]{"EXPENSE", "INCOME"});
        formPanel.add(cmbType);

        formPanel.add(new JLabel("Category:"));
        cmbCategory = new JComboBox<>(new String[]{"Food", "Transport", "Utilities", "Salary", "Entertainment", "Other"});
        cmbCategory.setEditable(true);
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
        btnSave.putClientProperty("JButton.buttonType", "roundRect");
        btnSave.setBackground(Color.decode("#3B82F6"));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Inter", Font.BOLD, 14));
        formPanel.add(btnSave);

        cardPanel.add(formPanel, BorderLayout.CENTER);
        
        add(cardPanel);
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
