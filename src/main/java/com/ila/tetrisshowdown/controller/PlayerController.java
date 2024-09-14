package com.ila.tetrisshowdown.controller;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ila.tetrisshowdown.PlayerCreateForm;
import com.ila.tetrisshowdown.PlayerLoginForm;
import com.ila.tetrisshowdown.entity.Player;
import com.ila.tetrisshowdown.service.PlayerService;

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

        try {
            playerService.create(playerCreateForm.getPlayername(), playerCreateForm.getEmail(), playerCreateForm.getPassword1());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "Duplicated User Information");
            return "signup_page";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_page"; // Return the login page
    }

    @PostMapping("/login")
    public String login(@Valid PlayerLoginForm playerLoginForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "login_page";
        }

        Optional<Player> player = playerService.login(playerLoginForm.getEmail(), playerLoginForm.getPassword());

        if (player.isPresent()) {
            // Login successful
            model.addAttribute("loginSuccess", "Login successful!");
            return "login_page";  // Make sure the view is correctly set to the login page
        } else {
            // Login failed
            model.addAttribute("loginError", "Login unsuccessful! Incorrect email or password.");
            return "login_page";  // Show login page with error message
        }
    }
}
