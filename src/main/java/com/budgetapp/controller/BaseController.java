package com.budgetapp.controller;

public abstract class BaseController {
    protected int currentUserID;
    protected boolean isAuthenticated;
    protected String errorMSG;
    protected int sessionTimeout;

    public int getCurrentUserID() { return currentUserID; }
    public boolean checkAuth() { return isAuthenticated; }
    public void handleError(String msg) { this.errorMSG = msg; }
}
