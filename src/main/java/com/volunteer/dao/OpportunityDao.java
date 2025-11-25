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
import com.volunteer.model.Opportunity;

public class OpportunityDao implements Repository<Opportunity> {
  public Opportunity save(Opportunity o) {
    try (Connection c = Database.getConnection()) {
      if (o.getId() == 0) {
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO opportunities(organization_id,position,description,date,location,status) VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
          ps.setLong(1, o.getOrganizationId());
          ps.setString(2, o.getPosition());
          ps.setString(3, o.getDescription());
          ps.setString(4, o.getDate().toString());
          ps.setString(5, o.getLocation());
          ps.setString(6, o.getStatus());
          ps.executeUpdate();
          try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) o.setId(rs.getLong(1));
          }
        }
      } else {
        try (PreparedStatement ps = c.prepareStatement("UPDATE opportunities SET organization_id=?, position=?, description=?, date=?, location=?, status=? WHERE id=?")) {
          ps.setLong(1, o.getOrganizationId());
          ps.setString(2, o.getPosition());
          ps.setString(3, o.getDescription());
          ps.setString(4, o.getDate().toString());
          ps.setString(5, o.getLocation());
          ps.setString(6, o.getStatus());
          ps.setLong(7, o.getId());
          ps.executeUpdate();
        }
      }
      return o;
    } catch (SQLException ex) {
      return o;
    }
  }

  public Opportunity findById(long id) {
    try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT id,organization_id,position,description,date,location,status FROM opportunities WHERE id=?")) {
      ps.setLong(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return map(rs);
      }
    } catch (SQLException ignored) {
    }
    return null;
  }

  public List<Opportunity> findAll() {
    List<Opportunity> list = new ArrayList<>();
    try (Connection c = Database.getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT id,organization_id,position,description,date,location,status FROM opportunities")) {
      while (rs.next()) list.add(map(rs));
    } catch (SQLException ignored) {
    }
    return list;
  }

  public boolean delete(long id) {
    try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM opportunities WHERE id=?")) {
      ps.setLong(1, id);
      return ps.executeUpdate() > 0;
    } catch (SQLException ignored) {
    }
    return false;
  }

  private Opportunity map(ResultSet rs) throws SQLException {
    long id = rs.getLong("id");
    long orgId = rs.getLong("organization_id");
    String position = rs.getString("position");
    String description = rs.getString("description");
    LocalDate date = LocalDate.parse(rs.getString("date"));
    String location = rs.getString("location");
    String status = rs.getString("status");
    return new Opportunity(id, orgId, position, description, date, location, status);
  }
}