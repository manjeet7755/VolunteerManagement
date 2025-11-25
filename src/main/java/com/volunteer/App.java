package com.volunteer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.volunteer.ui.LoginPanel;
import com.volunteer.db.DbInitializer;
import com.volunteer.util.Theme;

public class App {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception ignored) {
      }
      Theme.applyGlobal();
      DbInitializer.createSchema();
      JFrame frame = new JFrame("Volunteer Management");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(1000, 700);
      frame.setLocationRelativeTo(null);
      LoginPanel panel = new LoginPanel(frame);
      frame.setContentPane(panel);
      frame.setVisible(true);
    });
  }
}