package com.example.starter;

import com.example.starter.auth.LocalAuth;
import com.example.starter.entities.User;
import com.example.starter.handlers.TasksHandler;
import com.example.starter.handlers.WhoAmIHandler;
import com.example.starter.services.TasksService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.LocalSessionStore;

public class TasksVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);

    router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
    AuthenticationProvider authProvider = new LocalAuth();
    AuthenticationHandler basicAuthHandler = BasicAuthHandler.create(authProvider);

    router.route("/*").handler(basicAuthHandler);
    router.route().handler(BodyHandler.create());

//    router.get("/").handler(this::indexHandler);
    router.get("/").handler(ctx -> {ctx.put("title", "Tasks Home");
      ctx.response().putHeader("Content-Type", "text/plain");
      ctx.response().end("Tasks!");
    });
    loadData();
    router.get("/whoami").handler(new WhoAmIHandler());
    router.get("/users").handler(new TasksHandler.UserHandler());
    router.post("/users").handler(new TasksHandler.UserHandler());
    router.patch("/users/:id").handler(new TasksHandler.UserHandler());
    router.delete("/users/:id").handler(new TasksHandler.UserHandler());
    router.get("/users/:id/tasks").handler(new TasksHandler.UserTaskHandler());
    router.post("/users/:id/tasks").handler(new TasksHandler.UserTaskHandler());
    router.get("/tasks").handler(new TasksHandler.TaskHandler());

    HttpServer server = vertx.createHttpServer();
    int port = config().getInteger("http.port", 80);
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

  private void loadData() {
    try {
      TasksService ts = new TasksService();
      User u = ts.addUser("Juan Perez", "jperez", "12345", "juan@mail.com");
      ts.addTask(u.getId(), "Login", "Ingresa al portal con tus datos personales para registrar la asistencia.");
      ts.addTask(u.getId(), "Camaras", "Revisar las camaras de seguridad del dia anterior.");
      u = ts.addUser("Ana Santos", "asantos", "12345", "ana@mail.com");
      ts.addTask(u.getId(), "Login", "Ingresa al portal con tus datos personales para registrar la asistencia.");
      u = ts.addUser("Luis Rios", "lrios", "12345", "luis@mail.com");
      u = ts.addUser("Carlos Gomez", "cgomez", "12345", "carlos@mail.com");
      ts.addTask(u.getId(), "Login", "Ingresa al portal con tus datos personales para registrar la asistencia.");
      ts.addTask(u.getId(), "Escudos", "Verificar el estado de los escudos principales y calibrarlos.");
      ts.addTask(u.getId(), "Reactor", "Verificar la temperatura del reactor, si es muy alta abrir la exclusa de liberarcion.");
    } catch (Exception e) {
      throw new RuntimeException("No se pueden cargar usuarios!");
    }
  }
}
