package com.volunteer.model;

public class Volunteer extends User {
  public Volunteer(long id, String name, String email) {
    super(id, name, email, Role.VOLUNTEER);
  }
}