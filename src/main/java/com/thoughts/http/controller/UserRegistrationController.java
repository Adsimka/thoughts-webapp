package com.thoughts.http.controller;

import com.thoughts.dto.user.CreateUserDto;
import com.thoughts.service.UserService;
import com.thoughts.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class UserRegistrationController {

    private final UserService userService;
    private final VerificationTokenService tokenService;

    @GetMapping
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new CreateUserDto());
        }
        return "auth/registration";
    }

    @PostMapping
    public String registerUserAccount(@Validated @ModelAttribute("user") CreateUserDto user,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            bindingResult.rejectValue(
                    "confirmPassword",
                    "user.confirmPassword",
                    "Passwords do not match"
            );
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.user",
                    bindingResult
            );
            System.err.println(bindingResult.getAllErrors());
            return "redirect:/registration";
        }
        userService.registrationNewUser(user);
        return "auth/check-email";
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token,
                              Model model) {
        if (tokenService.validateVerificationToken(token)) {
            model.addAttribute(
                    "successMessage",
                    "Your account has been verified successfully."
            );
        } else {
            model.addAttribute(
                    "failMessage",
                    "Invalid verification token."
            );
        }
        return "auth/verified";
    }
}
