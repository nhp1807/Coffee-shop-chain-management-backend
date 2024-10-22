package com.example.coffee_shop_chain_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class TestController {
    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }

    @GetMapping("/home")
    public String handleWelcome(){
        return "home";
    }

    @GetMapping("/admin/home")
    public String handleAdminHome(){
        return "home_admin";
    }

    @GetMapping("/manager/home")
    public String handleManagerHome(){
        return "home_manager";
    }

    @GetMapping("/login")
    public String handleLogin(){
        return "custom_login";
    }
}
