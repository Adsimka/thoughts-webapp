package com.thoughts.http.controller;

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
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final UserService userService;

    @GetMapping
    public String showUserEdit(Model model) {
        var users = userService.getAllUsersWithRoles();
        model.addAttribute("users", users);

        return "admin/users";
    }

    @GetMapping("/{user}")
    public String showUserEditProfile(Model model,
                                      @PathVariable("user") User user) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "admin/edit";
    }

    @PostMapping("/{id}")
    public String userEditProfile(@PathVariable("id") Long id,
                                  @ModelAttribute("user") User user) {
        return userService.updateUserByAdmin(id, user)
                .map(u -> "redirect:/admin")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
