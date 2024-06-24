package com.thoughts.http.controller;

import com.thoughts.dto.MessageDto;
import com.thoughts.model.Message;
import com.thoughts.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public String findByAll(Model model) {
        List<Message> messages = messageService.findAll();
        model.addAttribute("messages", messages);

        return "message/messages";
    }

    @PostMapping
    public String create(@ModelAttribute("message") MessageDto message) {
        messageService.create(message);

        return "redirect:/messages";
    }

    @PostMapping("/tag")
    public String findByAll(Model model,
                            @RequestParam String tag) {
        var messages = messageService.findAll(tag);
        model.addAttribute("messages", messages);

        return "message/messages";
    }

    @GetMapping("/create")
    public String createMessage() {
        return "message/create";
    }
}
