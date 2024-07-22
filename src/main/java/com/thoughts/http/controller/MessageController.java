package com.thoughts.http.controller;

import com.thoughts.dto.message.CreateMessageDto;
import com.thoughts.model.User;
import com.thoughts.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public String showMessageForm(Model model,
                                  @RequestParam(required = false, defaultValue = "") String tag) {
        var messages = messageService.findAll(tag);
        model.addAttribute("messages", messages);

        if (!model.containsAttribute("message")) {
            model.addAttribute("message", new CreateMessageDto());
        }

        return "message/messages";
    }

    @PostMapping
    public String create(@ModelAttribute("message") @Validated CreateMessageDto message,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         @AuthenticationPrincipal User user) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", message);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.message",
                    bindingResult
            );
        } else {
            if (user != null) {
                messageService.create(message, user);
            }
        }

        return "redirect:/messages";
    }
}
