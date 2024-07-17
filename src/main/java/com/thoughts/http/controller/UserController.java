package com.thoughts.http.controller;

import com.thoughts.model.Role;
import com.thoughts.model.User;
import com.thoughts.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String users(Model model) {
        var users = userService.getAllUsersWithRoles();
        model.addAttribute("users", users);

        return "users/users";
    }

    @GetMapping("/{user}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showUserEditProfile(Model model,
                                  @PathVariable("user") User user) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "users/edit";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userEditProfile(@PathVariable("id") Long id,
                                  @ModelAttribute User user) {
        userService.update(id, user);

        return "redirect:/users";
    }

    @GetMapping("/{id}")
    public String showUserEditForm(Model model,
                                   @PathVariable("id") Long id) {
//        TODO
        return "users/profile";
    }
}

