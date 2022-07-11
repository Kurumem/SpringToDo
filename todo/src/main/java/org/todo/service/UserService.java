package org.todo.service;

import org.todo.model.*;

import java.util.List;
import java.util.Optional;

public interface UserService {
    boolean saveUser(User user);
    boolean saveRole(Role role);

    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    List<User> getUsers();
    boolean deleteUser(Long id);

    TodoList getTodoList(Long id);
    boolean saveTodoList(TodoList todoList);
    boolean deleteTodoList(Long id);

    Task getTask(Long id);
    boolean saveTask(Task task);
    boolean deleteTask(Long id);





}