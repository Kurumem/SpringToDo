package org.todo.controllers;

import lombok.RequiredArgsConstructor;
import org.todo.model.User;
import org.todo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") User userForm,
                          Model model) {
        if (!userForm.getPassword().equals(userForm.getConfirmPassword())) {
            model.addAttribute("confirmPasswordError",
                    "Пароли не совпадают");
            return "registration";
        }
        if (!userService.saveUser(userForm)) {
            model.addAttribute("usernameError",
                    "Пользователь с таким логином уже существует");
            return "registration";
        }
        userService.addRoleToUser(userForm.getUsername(), "ROLE_USER");
        return "redirect:/";
    }
}