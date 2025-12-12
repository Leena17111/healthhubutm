package com.secj3303.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/trainer")
public class TrainerController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "trainer-dashboard";
    }
}
