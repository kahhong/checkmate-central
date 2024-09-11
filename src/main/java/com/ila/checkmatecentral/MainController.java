package com.ila.checkmatecentral;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    
    @GetMapping("/")
    @ResponseBody
    public String index() {
        // System.out.println("Index");asdasda
        return "Bye fvvbasdasdasads!";
    }
}
