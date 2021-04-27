package com.example.starter.handlers;

import com.example.starter.entities.Task;
import com.example.starter.entities.User;
import com.example.starter.exceptions.TasksException;
import com.example.starter.services.TasksService;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TasksHandler {

  public static class UserHandler implements Handler<RoutingContext> {
    @Override
    public void handle(RoutingContext event) {
//      try {
//        Thread.sleep(10000);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
      TasksService ts = new TasksService();
      int userId;
      JsonObject body;
      switch (event.request().method().name()) {
        case "GET":
          event.response().putHeader("content-type", "application/json");
          event.response().setStatusCode(200);
          event.response().setStatusMessage("OK");
          event.response().end(ts.listUsers().encode());
          break;
        case "POST":
          body = event.getBodyAsJson();
          try {
            User user = ts.addUser(body.getString("name"), body.getString("userName"), body.getString("password"), body.getString("email"));
            event.response().putHeader("content-type", "application/json");
            event.response().setStatusCode(201);
            event.response().setStatusMessage("CREATED:" + user.getId());
            event.response().end();
          } catch (Exception e) {
            event.response().setStatusCode(400);
            event.response().setStatusMessage("ERROR");
            event.response().end(e.getMessage());
          }
          break;
        case "PATCH":
          body = event.getBodyAsJson();
          userId = Integer.parseInt(event.pathParam("id"));
          try {
            User user = ts.updateUser(userId, body.getString("name"), body.getString("password"), body.getString("email"));
            event.response().putHeader("content-type", "application/json");
            event.response().setStatusCode(200);
            event.response().setStatusMessage("OK");
            event.response().end(user.toJson().encode());
          } catch (TasksException e){
            event.response().setStatusCode(404);
            event.response().setStatusMessage("NOT_FOUND");
            event.response().end("Usuario con id:" + userId + " no encontrado!");
          } catch (Exception e) {
            event.response().setStatusCode(400);
            event.response().setStatusMessage("ERROR");
            event.response().end(e.getMessage());
          }
          break;
        case "DELETE":
          userId = Integer.parseInt(event.pathParam("id"));
          try {
            ts.deleteUser(userId);
            event.response().putHeader("content-type", "application/json");
            event.response().setStatusCode(204);
            event.response().setStatusMessage("DELETED");
            event.response().end();
          } catch (TasksException e){
            event.response().setStatusCode(404);
            event.response().setStatusMessage("NOT_FOUND");
            event.response().end("Usuario con id:" + userId + " no encontrado!");
          } catch (Exception e) {
            event.response().setStatusCode(400);
            event.response().setStatusMessage("ERROR");
            event.response().end(e.getMessage());
          }
          break;
      }
    }
  }

  public static class TaskHandler implements Handler<RoutingContext> {
    @Override
    public void handle(RoutingContext event) {
      TasksService ts = new TasksService();
      event.response().putHeader("content-type", "application/json");
      event.response().setStatusCode(200);
      event.response().setStatusMessage("OK");
      event.response().end(ts.listTasks().encode());
    }
  }

  public static class UserTaskHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext event) {
      TasksService ts = new TasksService();
      int userId;
      JsonArray body;
      switch (event.request().method().name()) {
        case "GET":
          userId = Integer.parseInt(event.pathParam("id"));
          try {
            User user = ts.getUser(userId);
            List<Task> userTasks = ts.getUserTasks(userId);
            JsonObject json = user.toJson();
            json.put("tasks", userTasks.stream().map(Task::toJson).collect(Collectors.toList()));
            event.response().putHeader("content-type", "application/json");
            event.response().setStatusCode(200);
            event.response().setStatusMessage("OK");
            event.response().end(json.encode());
          } catch (TasksException e){
            event.response().setStatusCode(404);
            event.response().setStatusMessage("NOT_FOUND");
            event.response().end("Usuario con id:" + userId + " no encontrado!");
          } catch (Exception e) {
            event.response().setStatusCode(400);
            event.response().setStatusMessage("ERROR");
            event.response().end(e.getMessage());
          }
          break;
        case "POST":
          body = event.getBodyAsJsonArray();
          userId = Integer.parseInt(event.pathParam("id"));

          try {
            body.forEach(o -> {
              JsonObject t = (JsonObject) o;
              try {
                ts.addTask(userId, t.getString("title"), t.getString("description"));
              } catch (TasksException e) {
                e.printStackTrace();
              }
            });
            event.response().putHeader("content-type", "application/json");
            event.response().setStatusCode(201);
            event.response().setStatusMessage("CREATED");
            event.response().end();
          } catch (Exception e) {
            event.response().setStatusCode(400);
            event.response().setStatusMessage("ERROR");
            event.response().end(e.getMessage());
          }
          break;
      }
    }
  }

//  public static class WhoAmIHandler implements Handler<RoutingContext> {
//    @Override
//    public void handle(RoutingContext event) {
//      event.response().putHeader("content-type", "application/json");
//      event.response().setStatusCode(200);
//      event.response().setStatusMessage("OK");
//      event.response().end(((User)event.user()).toJson().encode());
//    }
//  }
}
