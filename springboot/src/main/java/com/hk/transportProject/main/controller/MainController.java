package com.hk.transportProject.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

@Controller
public class MainController {
    
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @GetMapping("/main")
    public String mainPage(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "main";
        }
        return "redirect:/login";
    }
} 