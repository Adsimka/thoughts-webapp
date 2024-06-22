package com.thoughts.http.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/")
public class GreetingController {

    @GetMapping
    public String greeting(Model model) {
        return "greeting";
    }
}
