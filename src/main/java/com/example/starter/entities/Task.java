package com.example.starter.entities;

import io.vertx.core.json.JsonObject;

public class Task {
  private static int seq = 0;
  private int id;
  private String title;
  private String description;
  private boolean complete;

  public Task(String title, String description) {
    this.id = ++seq;
    this.title = title;
    this.description = description;
    this.complete = false;
  }

  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public boolean isComplete() {
    return complete;
  }

  public JsonObject toJson() {
    return JsonObject.mapFrom(this);
  }
}
