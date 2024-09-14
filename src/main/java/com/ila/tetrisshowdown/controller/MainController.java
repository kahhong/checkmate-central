package com.ila.tetrisshowdown.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    
    @GetMapping("/")
    public String index() {
        // System.out.println("Index");asdasda
        return "redirect:/player/login";
    }
}
