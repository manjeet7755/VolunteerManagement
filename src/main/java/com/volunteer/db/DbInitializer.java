package com.volunteer.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbInitializer {
  public static void createSchema() {
    try (Connection c = Database.getConnection(); Statement s = c.createStatement()) {
      s.executeUpdate("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, email TEXT UNIQUE NOT NULL, role TEXT NOT NULL)");
      s.executeUpdate("CREATE TABLE IF NOT EXISTS opportunities (id INTEGER PRIMARY KEY AUTOINCREMENT, organization_id INTEGER NOT NULL, description TEXT NOT NULL, date TEXT NOT NULL, location TEXT NOT NULL, status TEXT NOT NULL)");
      s.executeUpdate("CREATE TABLE IF NOT EXISTS signups (id INTEGER PRIMARY KEY AUTOINCREMENT, opportunity_id INTEGER NOT NULL, volunteer_id INTEGER NOT NULL, status TEXT NOT NULL)");
      s.executeUpdate("CREATE TABLE IF NOT EXISTS hour_logs (id INTEGER PRIMARY KEY AUTOINCREMENT, volunteer_id INTEGER NOT NULL, opportunity_id INTEGER NOT NULL, hours REAL NOT NULL, date TEXT NOT NULL)");
      s.executeUpdate("CREATE TABLE IF NOT EXISTS messages (id INTEGER PRIMARY KEY AUTOINCREMENT, from_user_id INTEGER NOT NULL, to_user_id INTEGER NOT NULL, body TEXT NOT NULL, sent_at TEXT NOT NULL)");
      // create a default admin/organization/volunteer and a sample opportunity if users table empty
      try (ResultSet rs = s.executeQuery("SELECT COUNT(*) as cnt FROM users")) {
        if (rs.next()) {
          int cnt = rs.getInt("cnt");
          if (cnt == 0) {
            s.executeUpdate("INSERT INTO users(name,email,role) VALUES('Admin','admin@example.com','ADMIN')");
            s.executeUpdate("INSERT INTO users(name,email,role) VALUES('Helping Org','org@example.com','ORGANIZATION')");
            s.executeUpdate("INSERT INTO users(name,email,role) VALUES('Alice Volunteer','alice@example.com','VOLUNTEER')");
            // insert sample opportunity (with position)
            s.executeUpdate("INSERT INTO opportunities(organization_id,position,description,date,location,status) VALUES(2,'Cleanup Crew','Community Cleanup','2025-12-01','Central Park','APPROVED')");
          }
        }
      }
      // ensure 'position' column exists on opportunities (add if missing)
      try (ResultSet cols = s.executeQuery("PRAGMA table_info(opportunities)")) {
        boolean hasPos = false;
        while (cols.next()) {
          String name = cols.getString("name");
          if ("position".equalsIgnoreCase(name)) { hasPos = true; break; }
        }
        if (!hasPos) {
          try {
            s.executeUpdate("ALTER TABLE opportunities ADD COLUMN position TEXT DEFAULT ''");
          } catch (SQLException ignore) {
            // ignore if alter not supported or already present
          }
        }
      }
    } catch (SQLException ignored) {
    }
  }
}