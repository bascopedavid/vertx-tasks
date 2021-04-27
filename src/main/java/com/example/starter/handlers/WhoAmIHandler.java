package com.example.starter.handlers;

import com.example.starter.entities.User;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class WhoAmIHandler implements Handler<RoutingContext> {
  @Override
  public void handle(RoutingContext event) {
    event.response().putHeader("content-type", "application/json");
    event.response().setStatusCode(200);
    event.response().setStatusMessage("OK");
    event.response().end(((User)event.user()).toJson().encode());
  }
}
