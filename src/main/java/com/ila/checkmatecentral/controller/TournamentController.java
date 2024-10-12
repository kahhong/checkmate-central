package com.ila.checkmatecentral.controller;

import java.util.List;

import com.ila.checkmatecentral.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.ila.checkmatecentral.entity.Tournament;
import com.ila.checkmatecentral.entity.TournamentStatus;
import com.ila.checkmatecentral.service.MatchService;
import com.ila.checkmatecentral.service.TournamentService;
import com.ila.checkmatecentral.service.UserAccountService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/tournaments")
public class TournamentController {
    public final TournamentService tournamentService;
    public final UserAccountService userAccountService;
    public final MatchService matchService;


/* Start of GET Mappings */

    @GetMapping({"/lists", "/list"})
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        return ResponseEntity.ok(tournaments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(tournamentService.getTournament(id));
    }

/* End of GET Mappings */




/* Start of POST Mappings */

    @CrossOrigin
    @PostMapping("/")
    public ResponseEntity<?> createTournament(@Valid @RequestBody Tournament tournament,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errorMessages = bindingResult.getFieldErrors();
            String errorBody =  errorMessages.isEmpty() ? "Invalid request body" : errorMessages.get(0).toString();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
        }
        tournamentService.create(tournament);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tournament Created Successfully");
    }

    /*
    @CrossOrigin
    @PostMapping("/{id}/add/{playerId}")
    public ResponseEntity<?> addPlayersToTournament(@PathVariable("id") Integer tournamentId, @PathVariable("playerId") Long playerId) {
        Tournament tournament = tournamentService.getTournament(tournamentId);
        try {
            if (tournament.getPlayerList().size() < tournament.getMaxPlayers() ) {
                tournamentService.addPlayer(tournamentId, playerId);
                return ResponseEntity.status(HttpStatus.OK).body("Player Added successfully");
            }else{
                return ResponseEntity.status(HttpStatus.OK).body("Tournament is full");
            }
        } catch (PlayerAlreadyInTournamentException e) {
            return ResponseEntity.status(HttpStatus.OK).body("Player is already in Tournament");
        }
        
    }
    */

    @CrossOrigin
    @PostMapping("/{id}/add")
    public ResponseEntity<?> addPlayersToTournament(@PathVariable("id") Integer tournamentId,
                                                    @RequestBody JsonNode json) {
        String email = json.get("email").asText();

        try{
            tournamentService.addPlayer(tournamentId, userAccountService.loadUserByUsername(email).getId());
            return ResponseEntity.status(HttpStatus.OK).body("Player Added successfully");
        }
        catch(TournamentStartedException | PlayerAlreadyInTournamentException | TournamentFullException e) {
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping("/{id}/start")
    public ResponseEntity<?> startTournament(@PathVariable("id") Integer tournamentId) {
        try{
            tournamentService.startTournament(tournamentId);
            return ResponseEntity.status(HttpStatus.OK).body("Tournament has started");
        }
        catch (InsufficientPlayersException e){
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        }
    }


    @CrossOrigin
    @PostMapping("/{id}/nextround")
    public ResponseEntity<?> nextRound(@PathVariable("id") Integer tournamentId) {
        return tournamentService.setNextRound(tournamentId);
    }

/* End of POST Mappings */


/* Start of PUT Mappings */

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTournament(@PathVariable("id") Integer tournamentId, @Valid @RequestBody Tournament updatedTournament,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errorMessages = bindingResult.getFieldErrors();
            String errorBody =  errorMessages.isEmpty() ? "Invalid request body" : errorMessages.get(0).toString();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
        }

        try {
            Tournament response = tournamentService.update(tournamentId, updatedTournament);
            return ResponseEntity.ok(response);
        } catch (TournamentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the tournament.");
        }
    }

/* End of PUT Mappings */


/* Start of DELETE Mappings */
    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTournament(@PathVariable Integer id) {
        try {
            if (tournamentService.getTournament(id) != null) {
                tournamentService.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).body("Tournament deleted successfully");
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tournament not found");
            }
        } catch (TournamentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tournament not found");
        }
    }

/* End of DELETE Mappings */

}
