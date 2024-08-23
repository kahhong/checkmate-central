package com.ila.tetrisshowdown;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    
    @GetMapping("/ila")
    @ResponseBody
    public String index() {
        // System.out.println("Index");
        return "Hello World!";
    }
}
