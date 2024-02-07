package com.github.julienma94.intellijplugintest.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ToolWindow {
    private JPanel panel1;
    private JButton ConnectButton;
    private JButton SettingsButton;

    public ToolWindow() {
        ConnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Connect");
            }
        });
        SettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Settings");
            }
        });
    }
}
