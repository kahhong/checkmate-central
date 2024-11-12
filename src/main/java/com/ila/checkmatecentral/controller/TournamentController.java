package com.ila.checkmatecentral.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.ila.checkmatecentral.entity.Admin;
import com.ila.checkmatecentral.entity.Player;
import com.ila.checkmatecentral.entity.Tournament;
import com.ila.checkmatecentral.entity.TournamentStatus;
import com.ila.checkmatecentral.service.AccountCredentialService;
import com.ila.checkmatecentral.service.MatchService;
import com.ila.checkmatecentral.service.PlayerService;
import com.ila.checkmatecentral.service.TournamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tournaments")
@CrossOrigin
public class TournamentController {
    public final TournamentService tournamentService;
    public final AccountCredentialService credentialService;
    public final MatchService matchService;
    public final PlayerService playerService;

    /* Start of GET Mappings */

    @GetMapping("/list")
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

    @PostMapping("/")
    public ResponseEntity<?> createTournament(@Valid @RequestBody Tournament tournament, BindingResult bindingResult, @AuthenticationPrincipal String userName) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }
        tournamentService.create(tournament, userName);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Tournament Created Successfully"));
    }

    @PutMapping("/{id}/add")
    public ResponseEntity<?> addPlayersToTournament(@PathVariable("id") Integer tournamentId,
            @RequestBody JsonNode json) {

        String email = json.get("email").asText();
        Tournament tournament = tournamentService.getTournament(tournamentId);
        
        if (tournament.getStatus() == TournamentStatus.ONGOING) {
            return ResponseEntity.status(HttpStatus.OK).body(
                Map.of("message", "Tournament is currently ongoing", "success", false));
        }

        Player player = (Player) credentialService.loadUserByUsername(email);
        if (!player.isAvailability()){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Player is currently unavailable", "success", false));
        }

        if (tournament.getPlayerList().size() < tournament.getMaxPlayers()) {
            tournamentService.addPlayer(tournamentId, player);
            return ResponseEntity.status(HttpStatus.OK).body(
                Map.of("message", "Player Added successfully", "success", true));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(
                Map.of("message","Tournament is full", "success", false));
        }
    }

    @PutMapping("/{id}/withdraw")
    public ResponseEntity<?> withdrawTournament(@PathVariable("id") Integer tournamentId,
            @RequestBody JsonNode json) {

        String email = json.get("email").asText();
        Tournament tournament = tournamentService.getTournament(tournamentId);

        if (tournament.getStatus() == TournamentStatus.ONGOING) {
            tournamentService.withdrawPlayer(tournamentId, (Player) credentialService.loadUserByUsername(email));
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Player Withdrawed successfully"));
        }

        if (tournament.getPlayerList().size() < tournament.getMaxPlayers()) {
            tournamentService.addPlayer(tournamentId, (Player) credentialService.loadUserByUsername(email));
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Player Withdrawed successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Tournament is full"));
        }
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<?> startTournament(@PathVariable("id") Integer tournamentId) {
        tournamentService.startTournament(tournamentId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Tournament has started"));
    }

    @PutMapping("/{id}/nextround")
    public ResponseEntity<?> nextRound(@PathVariable("id") Integer tournamentId) {
        tournamentService.setNextRound(tournamentId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Next round has started"));
    }

    /* End of POST Mappings */



    /* Start of PUT Mappings */

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTournament(@PathVariable("id") Integer tournamentId,
            @Valid @RequestBody Tournament updatedTournament, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<FieldError> errorMessages = bindingResult.getFieldErrors();
            String errorBody = errorMessages.isEmpty() ? "Invalid request body" : errorMessages.get(0).toString();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
        }

        Tournament response = tournamentService.update(tournamentId, updatedTournament);
        return ResponseEntity.ok(response);
    }

    /* End of PUT Mappings */



    /* Start of DELETE Mappings */

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTournament(@PathVariable Integer id) {
        if (!tournamentService.exists(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Tournament not found"));
        }

        tournamentService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Tournament deleted successfully"));
    }

    /* End of DELETE Mappings */
}
