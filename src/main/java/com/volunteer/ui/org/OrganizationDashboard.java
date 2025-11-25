package com.volunteer.ui.org;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import com.volunteer.dao.OpportunityDao;
import com.volunteer.dao.SignupDao;
import com.volunteer.model.Opportunity;
import com.volunteer.model.Signup;
import com.volunteer.model.User;
import com.volunteer.util.Theme;

public class OrganizationDashboard extends JPanel {
  private final JFrame frame;
  private final User current;
  private final OpportunityDao oppDao = new OpportunityDao();
  private final SignupDao signupDao = new SignupDao();

  public OrganizationDashboard(JFrame frame, User current) {
    this.frame = frame;
    this.current = current;
    setLayout(new BorderLayout());
    setBackground(Theme.BG);
    JTabbedPane tabs = new JTabbedPane();
    tabs.addTab("Post", postPanel());
    tabs.addTab("Signups", signupsPanel());
    add(tabs, BorderLayout.CENTER);
  }

  private JPanel postPanel() {
    JPanel p = new JPanel(new BorderLayout());
    p.setBackground(Theme.PANEL_BG);
    DefaultTableModel model = new DefaultTableModel(new Object[] {"ID","Position","Desc","Date","Location","Status"}, 0);
    JTable table = new JTable(model);
    Theme.styleTableHeader(table);
    refreshMyListings(model);
    JPanel form = new JPanel(new GridBagLayout());
    form.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
    form.setBackground(Theme.PANEL_BG);
    JTextField position = new JTextField();
    JTextField desc = new JTextField();
    JTextField date = new JTextField();
    JTextField location = new JTextField();
    JButton post = new JButton("Post");
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(4,4,4,4);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx=0;c.gridy=0;form.add(new JLabel("Position"),c);
    c.gridx=1;c.gridy=0;form.add(position,c);
    c.gridx=0;c.gridy=1;form.add(new JLabel("Description"),c);
    c.gridx=1;c.gridy=1;form.add(desc,c);
    c.gridx=0;c.gridy=1;form.add(new JLabel("Date yyyy-MM-dd"),c);
    c.gridx=1;c.gridy=1;form.add(date,c);
    c.gridx=0;c.gridy=2;form.add(new JLabel("Location"),c);
    c.gridx=1;c.gridy=2;form.add(location,c);
    c.gridx=1;c.gridy=3;form.add(post,c);
    post.addActionListener(e -> {
      Opportunity o = new Opportunity(0, current.getId(), position.getText().trim(), desc.getText().trim(), LocalDate.parse(date.getText().trim()), location.getText().trim(), "PENDING");
      oppDao.save(o);
      refreshMyListings(model);
    });
    Theme.styleButton(post);
    p.add(new JScrollPane(table), BorderLayout.CENTER);
    p.add(form, BorderLayout.EAST);
    return p;
  }

  private void refreshMyListings(DefaultTableModel model) {
    model.setRowCount(0);
    for (Opportunity o : oppDao.findAll()) {
      if (o.getOrganizationId() == current.getId()) {
        model.addRow(new Object[] {o.getId(), o.getDescription(), o.getDate(), o.getLocation(), o.getStatus()});
      }
    }
  }

  private JPanel signupsPanel() {
    JPanel p = new JPanel(new BorderLayout());
    p.setBackground(Theme.PANEL_BG);
    DefaultTableModel model = new DefaultTableModel(new Object[] {"Signup ID","Opp ID","Volunteer ID","Status"}, 0);
    JTable table = new JTable(model);
    Theme.styleTableHeader(table);
    refreshSignups(model);
    p.add(new JScrollPane(table), BorderLayout.CENTER);
    return p;
  }

  private void refreshSignups(DefaultTableModel model) {
    model.setRowCount(0);
    List<Signup> list = signupDao.findAll();
    for (Signup s : list) {
      model.addRow(new Object[] {s.getId(), s.getOpportunityId(), s.getVolunteerId(), s.getStatus()});
    }
  }
}