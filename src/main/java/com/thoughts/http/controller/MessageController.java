package com.thoughts.http.controller;

import com.thoughts.dto.message.MessageDto;
import com.thoughts.model.User;
import com.thoughts.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public String findByAll(Model model,
                            @RequestParam(required = false, defaultValue = "") String tag) {
        var messages = messageService.findAll(tag);
        model.addAttribute("messages", messages);

        return "message/messages";
    }

    @PostMapping
    public String create(@ModelAttribute("message") MessageDto message,
                         @AuthenticationPrincipal User user) {
        if (user != null) {
            messageService.create(message, user);
        }

        return "redirect:/messages";
    }
}
