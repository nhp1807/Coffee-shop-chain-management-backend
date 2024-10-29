package com.example.coffee_shop_chain_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }

    @GetMapping("/home")
    public String handleWelcome(){
        return "guest/home";
    }

    @GetMapping("/admin/home")
    public String handleAdminHome(){
        return "admin/home";
    }

    @GetMapping("/manager/home")
    public String handleManagerHome(){
        return "manager/home";
    }

    @GetMapping("/login")
    public String handleLogin(){
        return "guest/custom_login";
    }
}
