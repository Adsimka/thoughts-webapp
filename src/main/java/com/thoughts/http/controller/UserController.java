package com.thoughts.http.controller;

import com.thoughts.model.Role;
import com.thoughts.model.User;
import com.thoughts.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String users(Model model) {
        var users = userService.getAllUsersWithRoles();
        model.addAttribute("users", users);
        return "users/users";
    }

    @GetMapping("/{user}")
    public String userEditProfile(Model model,
                                  @PathVariable("user") User user) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "users/edit";
    }

    @PostMapping("/{user}")
    public String userEditProfile(@ModelAttribute User user) {
        userService.update(user);

        return "redirect:/users";
    }
}

