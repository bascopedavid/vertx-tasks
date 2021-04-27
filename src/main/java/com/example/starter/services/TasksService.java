package com.example.starter.services;

import com.example.starter.entities.Task;
import com.example.starter.entities.User;
import com.example.starter.exceptions.TasksException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.*;
import java.util.stream.Collectors;

public class TasksService {
  private static Map<User, List<Task>> users = new HashMap<>();

  public User getUser(int id) throws TasksException {
    return users.keySet().stream().filter(u -> u.getId() == id).findFirst().orElseThrow(() -> new TasksException("User with id " + id + " not found!"));
  }

  public JsonArray listUsers() {
    JsonArray out = new JsonArray();
    users.keySet().stream().sorted(Comparator.comparingInt(User::getId)).forEach(u -> out.add(u.toJson()));
    return out;
  }

  public User addUser(String name, String userName, String password, String email) throws Exception {
    if (users.keySet().stream().anyMatch(u -> u.getUserName().equals(userName))) {
      throw new Exception("Usuario existente!");
    }
    User user = new User(name, userName, password, email);
    users.putIfAbsent(user, new ArrayList<>());
    return user;
  }

  public User updateUser(int userId, String name, String password, String email) throws TasksException {
    User user = getUser(userId);
    List<Task> tasks = users.get(user);
    users.remove(user);
    user.setName(name);
    user.setPassword(password);
    user.setEmail(email);
    users.put(user, tasks);
    return user;
  }

  public void deleteUser(int userId) throws TasksException {
    users.remove(getUser(userId));
  }

  public void addTask(int userId, String title, String description) throws TasksException {
    addTask(getUser(userId), title, description);
  }

  public void addTask(User user, String title, String description) {
    users.get(user).add(new Task(title, description));
  }

  public User login(String userName, String password) {
    return users.keySet().stream().filter(user -> user.getUserName().equals(userName) && user.getPassword().equals(password)).findFirst().orElseThrow();
  }

  public JsonArray listTasks() {
    JsonArray out = new JsonArray();
    users.forEach((u, t) -> {
      JsonObject json = u.toJson();
      json.put("tasks", t.stream().sorted(Comparator.comparingInt(Task::getId)).map(Task::toJson).collect(Collectors.toList()));
      out.add(json);
    });
    return out;
  }

  public List<Task> getUserTasks(int userId) throws TasksException {
    return users.get(getUser(userId));
  }
}
