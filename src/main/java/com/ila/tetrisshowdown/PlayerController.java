package com.ila.tetrisshowdown;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/player")
public class PlayerController {
    public final PlayerService playerService;

    @GetMapping("/signup")
    public String signup(PlayerCreateForm playerCreateForm) {
        return "signup_page";
    }

    @PostMapping("/signup")
    public String signup(@Valid PlayerCreateForm playerCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_page";
        }

        if (!playerCreateForm.getPassword1().equals(playerCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "Password is not matched");
            return "signup_page";
        }

        playerService.create(playerCreateForm.getPlayername(), playerCreateForm.getEmail(), playerCreateForm.getPassword1());

        return "redirect:/";
    }
}
