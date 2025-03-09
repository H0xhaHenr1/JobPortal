package com.finalProjectLufthansa.JobPortal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Return the login page (e.g., login.html)
    }

    @GetMapping("/home")
    public String home() {
        return "home"; // Return the home page (e.g., home.html)
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied"; // Return the access-denied page (e.g., access-denied.html)
    }
}
