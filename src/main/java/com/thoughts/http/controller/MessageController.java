package com.thoughts.http.controller;

import com.thoughts.model.Message;
import com.thoughts.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public String findByAll(Model model) {
        List<Message> messages = messageService.findAll();
        model.addAttribute(messages);

        return "message/messages";
    }
}
