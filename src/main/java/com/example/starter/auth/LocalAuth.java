package com.example.starter.auth;


import com.example.starter.services.TasksService;
import io.vertx.core.AsyncResult;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.AuthenticationProvider;

public class LocalAuth implements AuthenticationProvider {

  @Override
  public void authenticate(JsonObject credentials, Handler<AsyncResult<User>> resultHandler) {
    TasksService ts = new TasksService();
    try {
      User user = ts.login(credentials.getString("username"), credentials.getString("password"));
      resultHandler.handle(Future.succeededFuture(user));
    } catch (Exception e) {
      resultHandler.handle(Future.failedFuture("FORBIDDEN"));
    }
  }

}
