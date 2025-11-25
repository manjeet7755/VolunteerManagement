package com.volunteer.model;

import java.time.LocalDateTime;

public class Message {
  private long id;
  private long fromUserId;
  private long toUserId;
  private String body;
  private LocalDateTime sentAt;

  public Message(long id, long fromUserId, long toUserId, String body, LocalDateTime sentAt) {
    this.id = id;
    this.fromUserId = fromUserId;
    this.toUserId = toUserId;
    this.body = body;
    this.sentAt = sentAt;
  }

  public long getId() { return id; }
  public void setId(long id) { this.id = id; }
  public long getFromUserId() { return fromUserId; }
  public void setFromUserId(long fromUserId) { this.fromUserId = fromUserId; }
  public long getToUserId() { return toUserId; }
  public void setToUserId(long toUserId) { this.toUserId = toUserId; }
  public String getBody() { return body; }
  public void setBody(String body) { this.body = body; }
  public LocalDateTime getSentAt() { return sentAt; }
  public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
}