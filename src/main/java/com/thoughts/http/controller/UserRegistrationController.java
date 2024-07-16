package com.thoughts.http.controller;

import com.thoughts.dto.CreateUserDto;
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
        userService.registrationNewUser(user);
        return "auth/check-email";
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token,
                              Model model) {
        if (tokenService.validateVerificationToken(token)) {
            model.addAttribute("successMessage", "Your account has been verified successfully.");
        } else {
            model.addAttribute("failMessage", "Invalid verification token.");
        }

        return "auth/verified";
    }
}
