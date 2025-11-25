package com.volunteer.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.volunteer.AppException;

public class Database {
  public static String getUrl() {
    String p = System.getProperty("db.url");
    if (p != null && !p.isEmpty()) return p;
    String e = System.getenv("DB_URL");
    if (e != null && !e.isEmpty()) return e;
    return "jdbc:sqlite:volunteer.db";
  }

  public static Connection getConnection() {
    try {
      return DriverManager.getConnection(getUrl());
    } catch (SQLException ex) {
      throw new AppException("Database connection failed", ex);
    }
  }
}