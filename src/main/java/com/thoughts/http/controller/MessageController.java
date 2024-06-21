package com.thoughts.http.controller;

import com.thoughts.dto.MessageDto;
import com.thoughts.model.Message;
import com.thoughts.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        return "message/create";
    }

    @PostMapping
    public String create(@RequestParam String text,
                         @RequestParam String tag) {
        MessageDto messageDto = MessageDto.builder()
                .text(text)
                .tag(tag)
                .build();

        messageService.create(messageDto);

        return "redirect:/create";
    }
}
