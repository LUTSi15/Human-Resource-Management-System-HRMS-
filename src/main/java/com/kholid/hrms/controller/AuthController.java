package com.kholid.hrms.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("roles", List.of("ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_MANAGER"));
        return "login";
    }

    // Explicit /error mapping
    @GetMapping("/error")
    public String error(Model model) {
        model.addAttribute("message", "Something went wrong!");
        return "error"; // make sure you have templates/error.html
    }
}
