package com.thoughts.http.controller;

import com.thoughts.dto.MessageDto;
import com.thoughts.model.Message;
import com.thoughts.model.User;
import com.thoughts.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public String findByAll(Model model,
                            @RequestParam(required = false, defaultValue = "") String tag) {
        List<Message> messages;

        if (tag != null) {
            messages = messageService.findAll(tag);
        } else {
            messages = messageService.findAll();
        }
        model.addAttribute("messages", messages);

        return "message/messages";
    }

    @PostMapping
    public String create(@ModelAttribute("message") MessageDto message,
                         @AuthenticationPrincipal User user,
                         @RequestParam("file") MultipartFile file) {
        if (user != null) {
            messageService.create(message, user, file);
        }

        return "redirect:/messages";
    }

    @GetMapping("/create")
    public String createMessage() {
        return "message/create";
    }
}
