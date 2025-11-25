package com.volunteer.model;

public class Organization extends User {
  public Organization(long id, String name, String email) {
    super(id, name, email, Role.ORGANIZATION);
  }
}