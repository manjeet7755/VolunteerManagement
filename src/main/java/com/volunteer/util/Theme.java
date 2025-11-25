package com.volunteer.util;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class Theme {
  public static final Color PRIMARY = new Color(30, 136, 229); // blue
  public static final Color ACCENT = new Color(255, 167, 38); // orange
  public static final Color BG = new Color(245, 247, 250); // light gray
  public static final Color PANEL_BG = new Color(255, 255, 255);
  public static final Color TEXT = new Color(33, 33, 33);

  public static void applyGlobal() {
    UIManager.put("TabbedPane.selected", PRIMARY);
    UIManager.put("Button.background", PRIMARY);
    UIManager.put("Button.foreground", java.awt.Color.WHITE);
  }

  public static void styleButton(JButton b) {
    b.setBackground(PRIMARY);
    b.setForeground(java.awt.Color.WHITE);
    b.setBorder(new EmptyBorder(6, 12, 6, 12));
    b.setFocusPainted(false);
    b.setOpaque(true);
    b.setContentAreaFilled(true);
    b.setBorderPainted(false);
    b.setFont(b.getFont().deriveFont(java.awt.Font.BOLD));
  }

  public static void styleTableHeader(JTable t) {
    JTableHeader h = t.getTableHeader();
    if (h != null) {
      h.setBackground(PRIMARY);
      h.setForeground(java.awt.Color.WHITE);
    }
  }
}
