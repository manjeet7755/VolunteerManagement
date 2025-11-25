package com.volunteer.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.volunteer.model.Role;
import com.volunteer.service.AuthService;
import com.volunteer.ui.admin.AdminDashboard;
import com.volunteer.ui.org.OrganizationDashboard;
import com.volunteer.ui.vol.VolunteerDashboard;
import com.volunteer.model.User;
import com.volunteer.util.Theme;

public class LoginPanel extends JPanel {
  private final JFrame frame;
  private final JTextField nameField = new JTextField();
  private final JTextField emailField = new JTextField();
  private final JComboBox<Role> roleBox = new JComboBox<>(Role.values());

  public LoginPanel(JFrame frame) {
    this.frame = frame;
    Theme.applyGlobal();
    setLayout(new BorderLayout());
    setBackground(Theme.BG);
    JPanel form = new JPanel(new GridBagLayout());
    form.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
    form.setBackground(Theme.PANEL_BG);
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(8, 8, 8, 8);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0; c.gridy = 0; {
      JLabel l = new JLabel("Name"); l.setForeground(Theme.TEXT); l.setFont(l.getFont().deriveFont(Font.PLAIN)); form.add(l, c);
    }
    c.gridx = 1; c.gridy = 0; form.add(nameField, c);
    c.gridx = 0; c.gridy = 1; {
      JLabel l = new JLabel("Email"); l.setForeground(Theme.TEXT); form.add(l, c);
    }
    c.gridx = 1; c.gridy = 1; form.add(emailField, c);
    c.gridx = 0; c.gridy = 2; {
      JLabel l = new JLabel("Role"); l.setForeground(Theme.TEXT); form.add(l, c);
    }
    c.gridx = 1; c.gridy = 2; form.add(roleBox, c);
    JButton login = new JButton("Continue");
    Theme.styleButton(login);
    c.gridx = 1; c.gridy = 3; form.add(login, c);
    add(form, BorderLayout.CENTER);

    AuthService auth = new AuthService();
    login.addActionListener(e -> {
      String name = nameField.getText().trim();
      String email = emailField.getText().trim();
      Role role = (Role) roleBox.getSelectedItem();
      User u = auth.loginOrCreate(name, email, role);
      if (u.getRole() == Role.ADMIN) frame.setContentPane(new AdminDashboard(frame, u));
      else if (u.getRole() == Role.ORGANIZATION) frame.setContentPane(new OrganizationDashboard(frame, u));
      else frame.setContentPane(new VolunteerDashboard(frame, u));
      frame.validate();
    });
  }
}