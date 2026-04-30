package com.budgetapp.controller;

import com.budgetapp.model.Notification;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class NotifController extends BaseController {
    private int unreadCount;
    private List<Notification> notifList;
    private Date lastSent;

    public String send(String msg, String type) { return ""; }
    public List<Notification> getAll() { return new ArrayList<>(); }
    public void markAsRead(int id) {}
    public int getUnreadCount() { return 0; }
    public void clear() {}
}
