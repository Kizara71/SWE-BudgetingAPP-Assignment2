package com.budgetapp.controller;

import com.budgetapp.view.IDashboardView;

public class DashboardController extends BaseController {
    private IDashboardView view;

    public DashboardController(IDashboardView view) {
        this.view = view;
    }

    public void updateView() {}
}
