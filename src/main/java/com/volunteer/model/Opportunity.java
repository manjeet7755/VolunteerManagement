package com.volunteer.model;

import java.time.LocalDate;

public class Opportunity {
  private long id;
  private long organizationId;
  private String position;
  private String description;
  private LocalDate date;
  private String location;
  private String status;

  public Opportunity(long id, long organizationId, String position, String description, LocalDate date, String location, String status) {
    this.id = id;
    this.organizationId = organizationId;
    this.position = position;
    this.description = description;
    this.date = date;
    this.location = location;
    this.status = status;
  }

  public long getId() { return id; }
  public void setId(long id) { this.id = id; }
  public long getOrganizationId() { return organizationId; }
  public void setOrganizationId(long organizationId) { this.organizationId = organizationId; }
  public String getPosition() { return position; }
  public void setPosition(String position) { this.position = position; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public LocalDate getDate() { return date; }
  public void setDate(LocalDate date) { this.date = date; }
  public String getLocation() { return location; }
  public void setLocation(String location) { this.location = location; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
}