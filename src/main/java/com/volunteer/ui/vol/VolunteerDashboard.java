package com.volunteer.ui.vol;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
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
import com.volunteer.dao.HourLogDao;
import com.volunteer.dao.OpportunityDao;
import com.volunteer.dao.SignupDao;
import com.volunteer.model.HourLog;
import com.volunteer.model.Opportunity;
import com.volunteer.model.Signup;
import com.volunteer.model.User;
import com.volunteer.util.Theme;

public class VolunteerDashboard extends JPanel {
  private final JFrame frame;
  private final User current;
  private final OpportunityDao oppDao = new OpportunityDao();
  private final SignupDao signupDao = new SignupDao();
  private final HourLogDao hourDao = new HourLogDao();

  public VolunteerDashboard(JFrame frame, User current) {
    this.frame = frame;
    this.current = current;
    setLayout(new BorderLayout());
    setBackground(Theme.BG);
    JTabbedPane tabs = new JTabbedPane();
    tabs.addTab("Opportunities", opportunitiesPanel());
    tabs.addTab("My Signups", mySignupsPanel());
    tabs.addTab("Hours", hoursPanel());
    add(tabs, BorderLayout.CENTER);
  }

  private JPanel opportunitiesPanel() {
    JPanel p = new JPanel(new BorderLayout());
    DefaultTableModel model = new DefaultTableModel(new Object[] {"ID","Org","Position","Desc","Date","Location","Status"}, 0);
    JTable table = new JTable(model);
    Theme.styleTableHeader(table);
    refreshListings(model);
    JButton signup = new JButton("Sign Up");
    signup.addActionListener(e -> {
      int row = table.getSelectedRow();
      if (row >= 0) {
        long id = Long.parseLong(model.getValueAt(row,0).toString());
        Signup s = new Signup(0, id, current.getId(), "PENDING");
        signupDao.save(s);
      }
    });
    Theme.styleButton(signup);
    p.add(new JScrollPane(table), BorderLayout.CENTER);
    p.add(signup, BorderLayout.SOUTH);
    return p;
  }

  private void refreshListings(DefaultTableModel model) {
    model.setRowCount(0);
    for (Opportunity o : oppDao.findAll()) {
      if ("APPROVED".equals(o.getStatus())) {
        model.addRow(new Object[] {o.getId(), o.getOrganizationId(), o.getPosition(), o.getDescription(), o.getDate(), o.getLocation(), o.getStatus()});
      }
    }
  }

  private JPanel mySignupsPanel() {
    JPanel p = new JPanel(new BorderLayout());
    DefaultTableModel model = new DefaultTableModel(new Object[] {"Signup ID","Opp ID","Status"}, 0);
    JTable table = new JTable(model);
    Theme.styleTableHeader(table);
    refreshMySignups(model);
    p.add(new JScrollPane(table), BorderLayout.CENTER);
    return p;
  }

  private void refreshMySignups(DefaultTableModel model) {
    model.setRowCount(0);
    for (Signup s : signupDao.findAll()) {
      if (s.getVolunteerId() == current.getId()) {
        model.addRow(new Object[] {s.getId(), s.getOpportunityId(), s.getStatus()});
      }
    }
  }

  private JPanel hoursPanel() {
    JPanel p = new JPanel(new BorderLayout());
    DefaultTableModel model = new DefaultTableModel(new Object[] {"ID","Opp ID","Hours","Date"}, 0);
    JTable table = new JTable(model);
    Theme.styleTableHeader(table);
    refreshHours(model);
    JPanel form = new JPanel(new GridBagLayout());
    form.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
    JTextField oppId = new JTextField();
    JTextField hours = new JTextField();
    JTextField date = new JTextField();
    JButton log = new JButton("Log");
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(4,4,4,4);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx=0;c.gridy=0;form.add(new JLabel("Opportunity ID"),c);
    c.gridx=1;c.gridy=0;form.add(oppId,c);
    c.gridx=0;c.gridy=1;form.add(new JLabel("Hours"),c);
    c.gridx=1;c.gridy=1;form.add(hours,c);
    c.gridx=0;c.gridy=2;form.add(new JLabel("Date yyyy-MM-dd"),c);
    c.gridx=1;c.gridy=2;form.add(date,c);
    c.gridx=1;c.gridy=3;form.add(log,c);
    log.addActionListener(e -> {
      long oid = Long.parseLong(oppId.getText().trim());
      double h = Double.parseDouble(hours.getText().trim());
      LocalDate d = LocalDate.parse(date.getText().trim());
      HourLog hl = new HourLog(0, current.getId(), oid, h, d);
      hourDao.save(hl);
      refreshHours(model);
    });
    Theme.styleButton(log);
    p.add(new JScrollPane(table), BorderLayout.CENTER);
    p.add(form, BorderLayout.EAST);
    return p;
  }

  private void refreshHours(DefaultTableModel model) {
    model.setRowCount(0);
    for (HourLog h : hourDao.findAll()) {
      if (h.getVolunteerId() == current.getId()) {
        model.addRow(new Object[] {h.getId(), h.getOpportunityId(), h.getHours(), h.getDate()});
      }
    }
  }
}