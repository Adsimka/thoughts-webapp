package com.thoughts.http.controller;

import com.thoughts.dto.user.CreateUserDto;
import com.thoughts.model.Role;
import com.thoughts.model.User;
import com.thoughts.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping("/admin/{user}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showUserEditProfile(Model model,
                                  @PathVariable("user") User user) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "users/edit";
    }

    @PostMapping("/admin/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userEditProfile(@PathVariable("id") Long id,
                                  @ModelAttribute User user) {
        return userService.updateUserByAdmin(id, user)
                .map(u -> "redirect:/users")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/profile/{id}")
    public String showUserEditForm(Model model,
                                   @PathVariable("id") Long id) {
        return userService.findById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    return "users/profile";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/profile/{id}")
    public String updateUserProfile(@PathVariable("id") Long id,
                                    @ModelAttribute CreateUserDto user) {
        return userService.updateProfile(id, user)
                .map(readUserDto -> "redirect:/users/profile/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}

