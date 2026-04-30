package com.budgetapp.view;

import java.util.List;
import java.awt.event.ActionListener;

public interface IDashboardView {
    void setBalance(double balance);
    void updateTransactionTable(List<?> data);
    void addTransactionListener(ActionListener l);
}
