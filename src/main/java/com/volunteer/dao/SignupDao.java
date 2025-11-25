package com.volunteer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.volunteer.db.Database;
import com.volunteer.model.Signup;

public class SignupDao implements Repository<Signup> {
  public Signup save(Signup s) {
    try (Connection c = Database.getConnection()) {
      if (s.getId() == 0) {
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO signups(opportunity_id,volunteer_id,status) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
          ps.setLong(1, s.getOpportunityId());
          ps.setLong(2, s.getVolunteerId());
          ps.setString(3, s.getStatus());
          ps.executeUpdate();
          try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) s.setId(rs.getLong(1));
          }
        }
      } else {
        try (PreparedStatement ps = c.prepareStatement("UPDATE signups SET opportunity_id=?, volunteer_id=?, status=? WHERE id=?")) {
          ps.setLong(1, s.getOpportunityId());
          ps.setLong(2, s.getVolunteerId());
          ps.setString(3, s.getStatus());
          ps.setLong(4, s.getId());
          ps.executeUpdate();
        }
      }
      return s;
    } catch (SQLException ex) {
      return s;
    }
  }

  public Signup findById(long id) {
    try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT id,opportunity_id,volunteer_id,status FROM signups WHERE id=?")) {
      ps.setLong(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return map(rs);
      }
    } catch (SQLException ignored) {
    }
    return null;
  }

  public List<Signup> findAll() {
    List<Signup> list = new ArrayList<>();
    try (Connection c = Database.getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT id,opportunity_id,volunteer_id,status FROM signups")) {
      while (rs.next()) list.add(map(rs));
    } catch (SQLException ignored) {
    }
    return list;
  }

  public boolean delete(long id) {
    try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM signups WHERE id=?")) {
      ps.setLong(1, id);
      return ps.executeUpdate() > 0;
    } catch (SQLException ignored) {
    }
    return false;
  }

  private Signup map(ResultSet rs) throws SQLException {
    long id = rs.getLong("id");
    long oppId = rs.getLong("opportunity_id");
    long volId = rs.getLong("volunteer_id");
    String status = rs.getString("status");
    return new Signup(id, oppId, volId, status);
  }
}