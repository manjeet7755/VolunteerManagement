package com.volunteer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.volunteer.db.Database;
import com.volunteer.model.Message;

public class MessageDao implements Repository<Message> {
  public Message save(Message m) {
    try (Connection c = Database.getConnection()) {
      if (m.getId() == 0) {
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO messages(from_user_id,to_user_id,body,sent_at) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
          ps.setLong(1, m.getFromUserId());
          ps.setLong(2, m.getToUserId());
          ps.setString(3, m.getBody());
          ps.setString(4, m.getSentAt().toString());
          ps.executeUpdate();
          try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) m.setId(rs.getLong(1));
          }
        }
      } else {
        try (PreparedStatement ps = c.prepareStatement("UPDATE messages SET from_user_id=?, to_user_id=?, body=?, sent_at=? WHERE id=?")) {
          ps.setLong(1, m.getFromUserId());
          ps.setLong(2, m.getToUserId());
          ps.setString(3, m.getBody());
          ps.setString(4, m.getSentAt().toString());
          ps.setLong(5, m.getId());
          ps.executeUpdate();
        }
      }
      return m;
    } catch (SQLException ex) {
      return m;
    }
  }

  public Message findById(long id) {
    try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT id,from_user_id,to_user_id,body,sent_at FROM messages WHERE id=?")) {
      ps.setLong(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return map(rs);
      }
    } catch (SQLException ignored) {
    }
    return null;
  }

  public List<Message> findAll() {
    List<Message> list = new ArrayList<>();
    try (Connection c = Database.getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT id,from_user_id,to_user_id,body,sent_at FROM messages")) {
      while (rs.next()) list.add(map(rs));
    } catch (SQLException ignored) {
    }
    return list;
  }

  public boolean delete(long id) {
    try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM messages WHERE id=?")) {
      ps.setLong(1, id);
      return ps.executeUpdate() > 0;
    } catch (SQLException ignored) {
    }
    return false;
  }

  private Message map(ResultSet rs) throws SQLException {
    long id = rs.getLong("id");
    long fromId = rs.getLong("from_user_id");
    long toId = rs.getLong("to_user_id");
    String body = rs.getString("body");
    LocalDateTime sentAt = LocalDateTime.parse(rs.getString("sent_at"));
    return new Message(id, fromId, toId, body, sentAt);
  }
}