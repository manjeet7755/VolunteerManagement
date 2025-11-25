package com.volunteer.service;

import com.volunteer.dao.UserDao;
import com.volunteer.model.Role;
import com.volunteer.model.User;

public class AuthService {
  private final UserDao users = new UserDao();

  public User loginOrCreate(String name, String email, Role role) {
    User existing = users.findByEmail(email);
    if (existing != null) return existing;
    User u = new User(0, name, email, role);
    return users.save(u);
  }
}