package com.thoughts.http.controller;

import com.thoughts.dto.user.CreateUserDto;
import com.thoughts.model.User;
import com.thoughts.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public String showProfile(Model model,
                              @PathVariable("id") Long id,
                              @AuthenticationPrincipal User currentUser) {
        return userService.profileFindById(id, currentUser)
                .map(user -> {
                    model.addAttribute("user", user);
                    return "users/profile";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/subscribe/{id}")
    public String subscribe(@PathVariable("id") Long id,
                            @AuthenticationPrincipal User currentUser) {
        userService.subscribe(id, currentUser);

        return "redirect:/profile/" + id;
    }

    @PostMapping("/unsubscribe/{id}")
    public String unsubscribe(@PathVariable("id") Long id,
                              @AuthenticationPrincipal User currentUser) {
        userService.unsubscribe(id, currentUser);

        return "redirect:/profile/" + id;
    }

    @GetMapping("/edit")
    public String showUserEditForm(Model model,
                                   @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);

        return "users/edit";
    }

    @PostMapping("/edit")
    public String updateUserProfile(@ModelAttribute CreateUserDto user,
                                    @RequestParam("id") Long id) {
        return userService.updateProfile(id, user)
                .map(readUserDto -> "redirect:/profile")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}

