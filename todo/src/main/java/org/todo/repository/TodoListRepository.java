package org.todo.repository;

import org.todo.model.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoListRepository extends JpaRepository<TodoList, Long> {
    TodoListRepository findByName(String name);
}
