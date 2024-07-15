package com.thoughts.http.controller;

import com.thoughts.dto.CreateUserDto;
import com.thoughts.model.User;
import com.thoughts.service.UserService;
import com.thoughts.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class UserRegistrationController {

    private final UserService userService;
    private final VerificationTokenService tokenService;

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new CreateUserDto());
        return "auth/registration";
    }

    @PostMapping
    public String registerUserAccount(Model model,
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

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token,
                              Model model) {
        String result = tokenService.validateVerificationToken(token);
        if (result.equals("valid")) {
            model.addAttribute("message", "Your account has been verified successfully.");
            return "auth/verified";
        }
        model.addAttribute("message", "Invalid verification token.");
        return "redirect:/login";
    }
}
