package com.thoughts.http.controller;

import com.thoughts.dto.CreateUserDto;
import com.thoughts.model.User;
import com.thoughts.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class UserRegistrationController {

    private final UserService userService;

    @GetMapping
    public String registration(Model model) {
        model.addAttribute("user", new CreateUserDto());
        return "auth/registration";
    }

    @PostMapping
    public String create(Model model,
                         @ModelAttribute("user") CreateUserDto user) {
        User userFromDb = userService.loadUserByUsername(user.getUsername());
        if (userFromDb != null) {
//            TODO 13.07.2024 : Sending an existing username to the service
            model.addAttribute("message", "User exists!");
            return "auth/registration";
        }
        userService.registrationNewUser(user);

        return "redirect:/login";
    }
}
