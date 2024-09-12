package com.ila.checkmatecentral.controller;

import com.ila.checkmatecentral.form.PlayerCreateForm;
import com.ila.checkmatecentral.form.TournamentCreateForm;
import com.ila.checkmatecentral.service.TournamentService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tournaments")
public class TournamentController {
    public final TournamentService tournamentService;

    @CrossOrigin
    @GetMapping("/")
    public String signup(PlayerCreateForm playerCreateForm) {
        return "signup_page";
    }


    @CrossOrigin
    @PostMapping("/")
    public String createTournament(@Valid @RequestBody TournamentCreateForm tournamentCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_page";
        }

        tournamentService.create(tournamentCreateForm);

        return "redirect:/";
    }
}
