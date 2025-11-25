package com.volunteer.ui.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import com.volunteer.dao.OpportunityDao;
import com.volunteer.dao.UserDao;
import com.volunteer.model.Opportunity;
import com.volunteer.model.Role;
import com.volunteer.model.User;
import com.volunteer.util.Theme;

public class AdminDashboard extends JPanel {
  private final JFrame frame;
  private final User current;
  private final UserDao userDao = new UserDao();
  private final OpportunityDao oppDao = new OpportunityDao();

  public AdminDashboard(JFrame frame, User current) {
    this.frame = frame;
    this.current = current;
    setLayout(new BorderLayout());
    setBackground(Theme.BG);
    JTabbedPane tabs = new JTabbedPane();
    tabs.addTab("Users", usersPanel());
    tabs.addTab("Listings", listingsPanel());
    tabs.addTab("Settings", settingsPanel());
    add(tabs, BorderLayout.CENTER);
  }

  private JPanel usersPanel() {
    JPanel p = new JPanel(new BorderLayout());
    p.setBackground(Theme.PANEL_BG);
    DefaultTableModel model = new DefaultTableModel(new Object[] {"ID","Name","Email","Role"}, 0);
    JTable table = new JTable(model);
    Theme.styleTableHeader(table);
    refreshUsers(model);
    JPanel form = new JPanel(new GridBagLayout());
    form.setBackground(Theme.PANEL_BG);
    form.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
    JTextField name = new JTextField();
    JTextField email = new JTextField();
    JComboBox<Role> role = new JComboBox<>(Role.values());
    JButton add = new JButton("Add");
    JButton remove = new JButton("Remove Selected");
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(4,4,4,4);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx=0;c.gridy=0;form.add(new JLabel("Name"),c);
    c.gridx=1;c.gridy=0;form.add(name,c);
    c.gridx=0;c.gridy=1;form.add(new JLabel("Email"),c);
    c.gridx=1;c.gridy=1;form.add(email,c);
    c.gridx=0;c.gridy=2;form.add(new JLabel("Role"),c);
    c.gridx=1;c.gridy=2;form.add(role,c);
    c.gridx=1;c.gridy=3;form.add(add,c);
    c.gridx=1;c.gridy=4;form.add(remove,c);
    add.addActionListener(e -> {
      User u = new User(0, name.getText().trim(), email.getText().trim(), (Role) role.getSelectedItem());
      userDao.save(u);
      refreshUsers(model);
    });
    remove.addActionListener(e -> {
      int row = table.getSelectedRow();
      if (row >= 0) {
        long id = Long.parseLong(model.getValueAt(row,0).toString());
        userDao.delete(id);
        refreshUsers(model);
      }
    });
    Theme.styleButton(add);
    Theme.styleButton(remove);
    p.add(new JScrollPane(table), BorderLayout.CENTER);
    p.add(form, BorderLayout.EAST);
    return p;
  }

  private void refreshUsers(DefaultTableModel model) {
    model.setRowCount(0);
    List<User> users = userDao.findAll();
    for (User u : users) model.addRow(new Object[] {u.getId(), u.getName(), u.getEmail(), u.getRole()});
  }

  private JPanel listingsPanel() {
    JPanel p = new JPanel(new BorderLayout());
    p.setBackground(Theme.PANEL_BG);
    DefaultTableModel model = new DefaultTableModel(new Object[] {"ID","Org","Position","Desc","Date","Location","Status"}, 0);
    JTable table = new JTable(model);
    Theme.styleTableHeader(table);
    refreshListings(model);
    JButton approve = new JButton("Approve");
    JButton reject = new JButton("Reject");
    approve.addActionListener(e -> updateListingStatus(table, model, "APPROVED"));
    reject.addActionListener(e -> updateListingStatus(table, model, "REJECTED"));
    Theme.styleButton(approve);
    Theme.styleButton(reject);
    JPanel actions = new JPanel();
    actions.setBackground(Theme.PANEL_BG);
    actions.add(approve);
    actions.add(reject);
    p.add(new JScrollPane(table), BorderLayout.CENTER);
    p.add(actions, BorderLayout.SOUTH);
    return p;
  }

  private void refreshListings(DefaultTableModel model) {
    model.setRowCount(0);
    for (Opportunity o : oppDao.findAll()) {
      model.addRow(new Object[] {o.getId(), o.getOrganizationId(), o.getPosition(), o.getDescription(), o.getDate(), o.getLocation(), o.getStatus()});
    }
  }

  private void updateListingStatus(JTable table, DefaultTableModel model, String status) {
    int row = table.getSelectedRow();
    if (row >= 0) {
      long id = Long.parseLong(model.getValueAt(row,0).toString());
      Opportunity o = oppDao.findById(id);
      if (o != null) {
        o.setStatus(status);
        oppDao.save(o);
        refreshListings(model);
      }
    }
  }

  private JPanel settingsPanel() {
    JPanel p = new JPanel(new BorderLayout());
    p.add(new JLabel("System settings"), BorderLayout.CENTER);
    return p;
  }
}