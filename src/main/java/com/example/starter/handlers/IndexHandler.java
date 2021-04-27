package com.example.starter.handlers;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class IndexHandler implements Handler<RoutingContext> {
  @Override
  public void handle(RoutingContext event) {
    event.put("title", "Tasks Home");
    event.response().putHeader("Content-Type", "text/plain");
    event.response().end("Tasks!");
  }
}
