package com.budgetapp.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import com.budgetapp.model.Goal;

public class GoalView extends JPanel {
    private JPanel goalsListPanel;
    private JButton btnAddGoal;
    private JTextField txtGoalName;
    private JTextField txtTargetAmount;
    private JButton btnSaveGoal;

    public GoalView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.decode("#F3F4F6")); // Match light theme

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.decode("#F3F4F6"));
        JLabel title = new JLabel("Your Financial Goals");
        title.setFont(new Font("Inter", Font.BOLD, 24));
        title.setForeground(Color.decode("#1F2937")); // Dark text
        
        btnAddGoal = new JButton("+ New Goal");
        btnAddGoal.setBackground(Color.decode("#3B82F6"));
        btnAddGoal.setForeground(Color.WHITE);
        btnAddGoal.setFocusPainted(false);
        
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(btnAddGoal, BorderLayout.EAST);
        
        // Goals List Panel
        goalsListPanel = new JPanel();
        goalsListPanel.setLayout(new BoxLayout(goalsListPanel, BoxLayout.Y_AXIS));
        goalsListPanel.setBackground(Color.decode("#F3F4F6"));
        
        JScrollPane scrollPane = new JScrollPane(goalsListPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.decode("#F3F4F6"));

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void displayGoals(List<Goal> goals) {
        goalsListPanel.removeAll();
        for (Goal g : goals) {
            JPanel card = new JPanel(new BorderLayout(10, 10));
            card.setBackground(Color.WHITE); // Card bg
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#D1D5DB"), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
            
            JLabel nameLbl = new JLabel(g.getGoalName() != null ? g.getGoalName() : "Unnamed Goal");
            nameLbl.setForeground(Color.decode("#1F2937"));
            nameLbl.setFont(new Font("Inter", Font.BOLD, 16));
            
            JLabel statusLbl = new JLabel(String.format("$%.2f / $%.2f", g.getCurrentAmount(), g.getTargetAmount()));
            statusLbl.setForeground(Color.decode("#6B7280"));
            
            JProgressBar progress = new JProgressBar(0, 100);
            progress.setValue((int)g.calcProgress());
            progress.setForeground(Color.decode("#10B981")); // Green for goal progress
            progress.setBackground(Color.decode("#E5E7EB"));
            
            JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
            infoPanel.setBackground(Color.WHITE);
            infoPanel.add(nameLbl);
            infoPanel.add(statusLbl);
            
            card.add(infoPanel, BorderLayout.CENTER);
            card.add(progress, BorderLayout.SOUTH);
            
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
            goalsListPanel.add(card);
            goalsListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        goalsListPanel.revalidate();
        goalsListPanel.repaint();
    }
    
    public void addGoalListener(ActionListener l) {
        btnAddGoal.addActionListener(l);
    }
}
