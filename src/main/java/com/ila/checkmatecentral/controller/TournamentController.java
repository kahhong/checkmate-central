package com.ila.checkmatecentral.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ila.checkmatecentral.entity.Tournament;
import com.ila.checkmatecentral.exceptions.TournamentNotFoundException;
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

    @GetMapping({"/lists", "/list"})
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        return ResponseEntity.ok(tournaments);
    }

    @GetMapping("/{id}")
    public Tournament getTournamentById(@PathVariable("id") Integer id) {
        return tournamentRepository.findById(id).
            orElseThrow(() -> new TournamentNotFoundException(id));
    }

    @CrossOrigin
    @PostMapping("/")
    public ResponseEntity<?> createTournament(@Valid @RequestBody TournamentCreateForm tournamentCreateForm,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid tournament data");
        }

        tournamentService.create(tournamentCreateForm);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created Successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTournament(@PathVariable("id") Integer tournamentId,
            @RequestBody TournamentCreateForm updatedTournamentCreateForm,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid tournament data");
        }

        try {
            Tournament updatedTournament = tournamentService.update(tournamentId, updatedTournamentCreateForm);
            return ResponseEntity.ok(updatedTournament);
        } catch (TournamentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the tournament.");
        }
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTournament(@PathVariable Integer id) {
        if (!tournamentRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tournament not found");
        }

        tournamentService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Tournament deleted successfully");
    }
    
}
