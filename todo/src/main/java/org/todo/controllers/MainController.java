package org.todo.controllers;

import lombok.RequiredArgsConstructor;
import org.todo.model.Task;
import org.todo.model.TodoList;

import org.todo.model.User;
import org.todo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private TodoList todoListToView;
    private Task taskToView = new Task();

    @GetMapping("/main")
    public String main(Model model, Principal principal) {
        User user = userService.getUser(principal.getName());


        model.addAttribute("activeUser", user);
        //Добавление новых
        model.addAttribute("todoListForm", new TodoList());
        model.addAttribute("taskForm", new Task());

        model.addAttribute("todoListToView", todoListToView);
        model.addAttribute("taskToView", taskToView);

        return "main";
    }

    //Добавить список задач
    @PostMapping("/main/add/todoList")
    public String addTodoList(@ModelAttribute("todoListForm") TodoList todoList,
                              Model model, Principal principal) {
        User user = userService.getUser(principal.getName());
        userService.saveTodoList(todoList);
        user.getTodoLists().add(todoList);
        //Проверка на пустую строку
        for(TodoList todoList1 : user.getTodoLists()) {
            if(todoList1.getName().isEmpty()){
                user.getTodoLists().remove(todoList);
                return "redirect:/main";
            }
        }
        userService.saveUser(userService.getUser(principal.getName()));
        return "redirect:/main";
    }


    //Добавление задач
    @PostMapping("/main/todoList/{todoListId}/add/task")
    public String addTask(@PathVariable("todoListId") long todoListId,
                                         @ModelAttribute("taskForm") Task task,
                                         Model model, Principal principal) {
        User user = userService.getUser(principal.getName());
        if (userService.saveTask(task)) {
            for (TodoList todoList : user.getTodoLists()) {
                if (todoList.getId() == todoListId) {
                    todoList.getTasks().add(task);
                    //Проверка на пустую строку
                    for(Task task1 : todoList.getTasks()) {
                        if(task1.getName().isEmpty()){
                            todoList.getTasks().remove(task);
                            return "redirect:/main";
                        }
                    }
                    userService.saveTodoList(todoList);
                    todoListToView = todoList;
                    break;
                }
            }
        }
        return "redirect:/main";
    }



    //Удаление списка задач
    @PostMapping("/main/remove/todoList/{todoListId}")
    public String deleteTodoList(@PathVariable("todoListId") long todoListId,
                                 Model model, Principal principal) {
        User user = userService.getUser(principal.getName());
        TodoList todoListToDelete = new TodoList();
        for (TodoList todoList : user.getTodoLists()) {
            if (todoList.getId() == todoListId) {
                todoListToDelete = todoList;
            }
        }
        user.getTodoLists().remove(todoListToDelete);
        userService.saveUser(user);
        try {
            if (todoListToDelete.getId().longValue() == todoListToView.getId().longValue()) {
                todoListToView = null;
            }
        } catch (Exception e){
            return "redirect:/main";
        }
        return "redirect:/main";
    }



    //Удаление задач
    @PostMapping("/main/remove/task/{taskId}")
    public String deleteTask(@PathVariable("taskId") long taskId,
                                  Model model, Principal principal) {
        User user = userService.getUser(principal.getName());
        for (TodoList todoList : user.getTodoLists()) {
                for (Task task : todoList.getTasks()) {
                    if (task.getId() == taskId) {
                        userService.deleteTask(taskId);
                        todoList.getTasks().remove(task);
                        userService.saveUser(user);
                        todoListToView = todoList;
                        break;
                    }
            }
        }
        return "redirect:/main";
    }




    //Список
    @PostMapping("/main/view/todoList/{id}")
    public String viewTodoList(@PathVariable long id,
                                Principal principal) {
        User user = userService.getUser(principal.getName());
        for (TodoList todoList : user.getTodoLists()) {
            if (todoList.getId() == id) {
                todoListToView = todoList;
                break;
            }
        }
        return "redirect:/main";
    }

    @PostMapping("/main/task/{id}/set/status")
    public String taskIsDone(@PathVariable("id") long id,
                              @ModelAttribute("doneStatus") String doneStatus,
                              Principal principal) {
        User user = userService.getUser(principal.getName());
        Task taskToSetStatus = userService.getTask(id);

        for (TodoList todoList : user.getTodoLists()) {
            for (Task task : todoList.getTasks()) {
                if (task.getId() == id) {

                    taskToSetStatus.setIsDone("on".equals(doneStatus));
                    System.out.println(taskToSetStatus.getIsDone());
                    userService.saveTask(taskToSetStatus);
                    userService.saveUser(user);
                    todoListToView = todoList;
                    break;
                }
            }
        }


        return "redirect:/main";
    }


}
