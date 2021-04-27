package com.example.starter;

import com.example.starter.handlers.IndexHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);

    router.get("/").handler(new IndexHandler());
    router.post("/users").handler( c -> {

    });
//      router.get("/").handler(this::indexHandler);
//    router.get("/").handler(ctx -> {ctx.put("title", "Tasks Home");
//      ctx.response().putHeader("Content-Type", "text/plain");
//      ctx.response().end("Tasks!");
//    });
    HttpServer server = vertx.createHttpServer();
    int port = config().getInteger("http.port", 80);
//    int port = 8888;
    server
      .requestHandler(router)
      .listen(port, ar -> {
        if (ar.succeeded()) {
          startPromise.complete();
          System.out.println("HTTP server started on port " + port);
        } else {
          startPromise.fail(ar.cause());
        }
      });
  }

  private void indexHandler(RoutingContext context) {
    context.put("title", "Tasks Home");
    context.response().putHeader("Content-Type", "text/plain");
    context.response().end("Tasks!");
  }
}
