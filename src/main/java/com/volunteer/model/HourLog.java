package com.volunteer.model;

import java.time.LocalDate;

public class HourLog {
  private long id;
  private long volunteerId;
  private long opportunityId;
  private double hours;
  private LocalDate date;

  public HourLog(long id, long volunteerId, long opportunityId, double hours, LocalDate date) {
    this.id = id;
    this.volunteerId = volunteerId;
    this.opportunityId = opportunityId;
    this.hours = hours;
    this.date = date;
  }

  public long getId() { return id; }
  public void setId(long id) { this.id = id; }
  public long getVolunteerId() { return volunteerId; }
  public void setVolunteerId(long volunteerId) { this.volunteerId = volunteerId; }
  public long getOpportunityId() { return opportunityId; }
  public void setOpportunityId(long opportunityId) { this.opportunityId = opportunityId; }
  public double getHours() { return hours; }
  public void setHours(double hours) { this.hours = hours; }
  public LocalDate getDate() { return date; }
  public void setDate(LocalDate date) { this.date = date; }
}