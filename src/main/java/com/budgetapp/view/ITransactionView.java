package com.budgetapp.view;

import java.awt.event.ActionListener;

public interface ITransactionView {
    double getAmount();
    String getCategory();
    void addSaveListener(ActionListener l);
    void clearFields();
}
