package com.ila.checkmatecentral.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ila.checkmatecentral.entity.Tournament;
import com.ila.checkmatecentral.form.PlayerCreateForm;
import com.ila.checkmatecentral.form.TournamentCreateForm;
import com.ila.checkmatecentral.repository.TournamentRepository;
import com.ila.checkmatecentral.service.TournamentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tournaments")
public class TournamentController {
    public final TournamentService tournamentService;
    public final TournamentRepository tournamentRepository;

    @CrossOrigin
    @GetMapping("/")
    public String signup(PlayerCreateForm playerCreateForm) {
        return "signup_page";
    }


    @CrossOrigin
    @PostMapping("/")
    public ResponseEntity<?> createTournament(@Valid @RequestBody TournamentCreateForm tournamentCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid tournament data");
        }

        tournamentService.create(tournamentCreateForm);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created Successfully");
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<Tournament> tournamentList = this.tournamentRepository.findAll();
        model.addAttribute("tournamentList", tournamentList);
        return "tournament_list";
    }
}
