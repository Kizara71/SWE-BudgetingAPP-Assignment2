package com.budgetapp.model;

import java.util.Date;

public class Notification {
    private int notificationID;
    private int userID;
    private String type;
    private String message;
    private boolean isRead;
    private Date timestamp;

    public void markAsRead() { this.isRead = true; }
}
