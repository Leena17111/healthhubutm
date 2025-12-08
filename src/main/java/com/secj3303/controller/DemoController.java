package com.secj3303.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {

    @GetMapping("/demo/home")
    public String home(Model model) {
        model.addAttribute("message", "This is SECJ3303…");
        return "demo";   // maps to WEB-INF/views/demo.html
    }
}
