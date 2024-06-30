package com.thoughts.http.controller;

import com.thoughts.model.Role;
import com.thoughts.model.User;
import com.thoughts.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class UserRegistrationController {

    private final UserService userService;

    @GetMapping
    public String registration() {
        return "auth/registration";
    }

    @PostMapping
    public String create(Model model,
                         @ModelAttribute User user) {
        User userFromDb = userService.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.addAttribute("message", "User exists!");

            return "auth/registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userService.save(user);

        return "redirect:/login";
    }
}
