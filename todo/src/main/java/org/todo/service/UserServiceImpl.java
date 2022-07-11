package org.todo.service;

import lombok.RequiredArgsConstructor;
import org.todo.model.*;
import org.todo.repository.*;
import org.todo.repository.TodoListRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final TodoListRepository todoListRepository;
    private final TaskRepository taskRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public boolean saveUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null ) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean saveRole(Role role) {
        if (roleRepository.findByName(role.getName()) != null) {
            return false;
        }
        roleRepository.save(role);
        return true;
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean deleteUser(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public TodoList getTodoList(Long id) {
        return todoListRepository.findById(id).orElse(new TodoList());
    }

    @Override
    public boolean saveTodoList(TodoList todoList) {
        this.todoListRepository.save(todoList);
        return true;
    }

    @Override
    public boolean deleteTodoList(Long id) {
        if (todoListRepository.findById(id).isPresent()) {
            todoListRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Task getTask(Long id) {
        return taskRepository.findById(id).orElse(new Task());
    }

    @Override
    public boolean saveTask(Task task) {
        taskRepository.save(task);
        return true;
    }

    @Override
    public boolean deleteTask(Long id) {
        if (taskRepository.findById(id).isPresent()) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }


}
