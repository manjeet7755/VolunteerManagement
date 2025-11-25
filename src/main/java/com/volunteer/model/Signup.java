package com.volunteer.model;

public class Signup {
  private long id;
  private long opportunityId;
  private long volunteerId;
  private String status;

  public Signup(long id, long opportunityId, long volunteerId, String status) {
    this.id = id;
    this.opportunityId = opportunityId;
    this.volunteerId = volunteerId;
    this.status = status;
  }

  public long getId() { return id; }
  public void setId(long id) { this.id = id; }
  public long getOpportunityId() { return opportunityId; }
  public void setOpportunityId(long opportunityId) { this.opportunityId = opportunityId; }
  public long getVolunteerId() { return volunteerId; }
  public void setVolunteerId(long volunteerId) { this.volunteerId = volunteerId; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
}