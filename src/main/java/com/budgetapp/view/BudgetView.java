package com.budgetapp.view;

import javax.swing.*;
import java.util.Map;
import java.util.HashMap;

public class BudgetView extends JPanel implements IBudgetView {
    private Map<String, JProgressBar> progressBars;
    private JButton btnSetLimit;

    public BudgetView() {
        progressBars = new HashMap<>();
        btnSetLimit = new JButton();
    }

    @Override
    public void updateProgress(String category, double percent) {}

    @Override
    public void showWarning(String msg) {}
}
