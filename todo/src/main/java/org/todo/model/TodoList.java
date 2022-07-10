package org.todo.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "t_todoList")
@Data
@RequiredArgsConstructor
public class TodoList {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    @OneToMany(fetch = EAGER, cascade = ALL)
    private List<Task> tasks = new ArrayList<>();
}
