package com.volunteer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.volunteer.db.Database;
import com.volunteer.model.HourLog;

public class HourLogDao implements Repository<HourLog> {
  public HourLog save(HourLog h) {
    try (Connection c = Database.getConnection()) {
      if (h.getId() == 0) {
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO hour_logs(volunteer_id,opportunity_id,hours,date) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
          ps.setLong(1, h.getVolunteerId());
          ps.setLong(2, h.getOpportunityId());
          ps.setDouble(3, h.getHours());
          ps.setString(4, h.getDate().toString());
          ps.executeUpdate();
          try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) h.setId(rs.getLong(1));
          }
        }
      } else {
        try (PreparedStatement ps = c.prepareStatement("UPDATE hour_logs SET volunteer_id=?, opportunity_id=?, hours=?, date=? WHERE id=?")) {
          ps.setLong(1, h.getVolunteerId());
          ps.setLong(2, h.getOpportunityId());
          ps.setDouble(3, h.getHours());
          ps.setString(4, h.getDate().toString());
          ps.setLong(5, h.getId());
          ps.executeUpdate();
        }
      }
      return h;
    } catch (SQLException ex) {
      return h;
    }
  }

  public HourLog findById(long id) {
    try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT id,volunteer_id,opportunity_id,hours,date FROM hour_logs WHERE id=?")) {
      ps.setLong(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return map(rs);
      }
    } catch (SQLException ignored) {
    }
    return null;
  }

  public List<HourLog> findAll() {
    List<HourLog> list = new ArrayList<>();
    try (Connection c = Database.getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT id,volunteer_id,opportunity_id,hours,date FROM hour_logs")) {
      while (rs.next()) list.add(map(rs));
    } catch (SQLException ignored) {
    }
    return list;
  }

  public boolean delete(long id) {
    try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM hour_logs WHERE id=?")) {
      ps.setLong(1, id);
      return ps.executeUpdate() > 0;
    } catch (SQLException ignored) {
    }
    return false;
  }

  private HourLog map(ResultSet rs) throws SQLException {
    long id = rs.getLong("id");
    long volId = rs.getLong("volunteer_id");
    long oppId = rs.getLong("opportunity_id");
    double hours = rs.getDouble("hours");
    LocalDate date = LocalDate.parse(rs.getString("date"));
    return new HourLog(id, volId, oppId, hours, date);
  }
}