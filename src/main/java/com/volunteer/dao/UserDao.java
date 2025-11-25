package com.volunteer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.volunteer.db.Database;
import com.volunteer.model.Role;
import com.volunteer.model.User;

public class UserDao implements Repository<User> {
  public User save(User u) {
    try (Connection c = Database.getConnection()) {
      if (u.getId() == 0) {
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO users(name,email,role) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
          ps.setString(1, u.getName());
          ps.setString(2, u.getEmail());
          ps.setString(3, u.getRole().name());
          ps.executeUpdate();
          try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) u.setId(rs.getLong(1));
          }
        }
      } else {
        try (PreparedStatement ps = c.prepareStatement("UPDATE users SET name=?, email=?, role=? WHERE id=?")) {
          ps.setString(1, u.getName());
          ps.setString(2, u.getEmail());
          ps.setString(3, u.getRole().name());
          ps.setLong(4, u.getId());
          ps.executeUpdate();
        }
      }
      return u;
    } catch (SQLException ex) {
      return u;
    }
  }

  public User findById(long id) {
    try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT id,name,email,role FROM users WHERE id=?")) {
      ps.setLong(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return map(rs);
      }
    } catch (SQLException ignored) {
    }
    return null;
  }

  public User findByEmail(String email) {
    try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT id,name,email,role FROM users WHERE email=?")) {
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return map(rs);
      }
    } catch (SQLException ignored) {
    }
    return null;
  }

  public List<User> findAll() {
    List<User> list = new ArrayList<>();
    try (Connection c = Database.getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT id,name,email,role FROM users")) {
      while (rs.next()) list.add(map(rs));
    } catch (SQLException ignored) {
    }
    return list;
  }

  public boolean delete(long id) {
    try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM users WHERE id=?")) {
      ps.setLong(1, id);
      return ps.executeUpdate() > 0;
    } catch (SQLException ignored) {
    }
    return false;
  }

  private User map(ResultSet rs) throws SQLException {
    long id = rs.getLong("id");
    String name = rs.getString("name");
    String email = rs.getString("email");
    Role role = Role.valueOf(rs.getString("role"));
    return new User(id, name, email, role);
  }
}