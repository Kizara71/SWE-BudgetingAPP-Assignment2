package com.budgetapp.controller;

public class AuthController extends BaseController {
    private String authToken;
    private boolean failedAttempts;

    public boolean register(String name, String email, String password) { return false; }
    public boolean login(String email, String password) { return false; }
    public void logout() {}
    public boolean validation() { return false; }
}
