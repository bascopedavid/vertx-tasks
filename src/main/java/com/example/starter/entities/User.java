package com.example.starter.entities;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.authorization.Authorization;

import java.util.Objects;

public class User implements io.vertx.ext.auth.User {
  private static int seq = 0;
  private int id;
  private String name;
  private String userName;
  private String password;
  private String email;

  public User(String name, String userName, String password, String email) {
    this.id = ++seq;
    this.name = name;
    this.userName = userName;
    this.password = password;
    this.email = email;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getUserName() {
    return userName;
  }

  public String getPassword() {
    return password;
  }

  public String getEmail() {
    return email;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public JsonObject toJson() {
    JsonObject json = JsonObject.mapFrom(this);
    json.remove("password");
    return json;
  }

  @Override
  public JsonObject attributes() {
    return null;
  }

  @Override
  public io.vertx.ext.auth.User isAuthorized(Authorization authority, Handler<AsyncResult<Boolean>> resultHandler) {
    return null;
  }

  @Override
  public JsonObject principal() {
    return null;
  }

  @Override
  public void setAuthProvider(AuthProvider authProvider) {
    System.out.println("asd");
  }
}
