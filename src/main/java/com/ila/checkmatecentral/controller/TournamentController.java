package com.ila.checkmatecentral.controller;

import java.util.List;

import com.ila.checkmatecentral.entity.UserAccount;
import com.ila.checkmatecentral.exceptions.MatchesNotCompletedException;
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
import com.ila.checkmatecentral.exceptions.TournamentNotFoundException;
import com.ila.checkmatecentral.repository.TournamentRepository;
import com.ila.checkmatecentral.repository.UserAccountRepository;
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
    public final UserAccountRepository userAccountRepository;
    public final TournamentRepository tournamentRepository;


/* Start of GET Mappings */

    @GetMapping({"/lists", "/list"})
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        return ResponseEntity.ok(tournaments);
    }

    @GetMapping("/{id}")
    public Tournament getTournamentById(@PathVariable("id") Integer id) {
        return tournamentRepository.findById(id)
            .orElseThrow(() -> new TournamentNotFoundException(id));
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
        int numPlayers = tournament.getMaxPlayers();
        // Bitwise operation
        // Check if numPlayers is greater than 0 and if n & (n - 1) equals 0
        if (numPlayers > 0 && (numPlayers & (numPlayers - 1)) != 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Number of players must be power of 2");
        }

        tournamentService.create(tournament);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tournament Created Successfully");
    }

    @CrossOrigin
    @PostMapping("/{id}/add/{playerId}")
    public ResponseEntity<?> addPlayersToTournament(@PathVariable("id") Integer tournamentId, @PathVariable("playerId") Long playerId) {
        Tournament tournament = tournamentService.getTournament(tournamentId);
        if (tournament.getPlayerList().size() < tournament.getMaxPlayers() ) {
            tournamentService.addPlayer(tournamentId, userAccountService.loadUserById(playerId));
            return ResponseEntity.status(HttpStatus.OK).body("Player Added successfully");
        }else{
            return ResponseEntity.status(HttpStatus.OK).body("Tournament is full");
        }
    }

    @CrossOrigin
    @PostMapping("/{id}/add")
    public ResponseEntity<?> addPlayersToTournament(@PathVariable("id") Integer tournamentId,
                                                    @RequestBody JsonNode json) {
        String email = json.get("email").asText();
        Tournament tournament = tournamentService.getTournament(tournamentId);
        if (tournament.getPlayerList().size() < tournament.getMaxPlayers()) {
            tournamentService.addPlayer(tournamentId, userAccountService.loadUserByUsername(email));
            return ResponseEntity.status(HttpStatus.OK).body("Player Added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Tournament is full");
        }
    }

    @CrossOrigin
    @PostMapping("/{id}/start")
    public ResponseEntity<?> startTournament(@PathVariable("id") Integer tournamentId) {
        Tournament tournament = tournamentService.getTournament(tournamentId);
//         ensure that tournament size is full before starting
        if (tournament.getPlayerList().size() == tournament.getMaxPlayers()) {
            tournament.setStatus(TournamentStatus.ONGOING);
            matchService.createMatches(tournament.getPlayerList(), 1, tournamentId);
            return ResponseEntity.status(HttpStatus.OK).body("Tournament has started");
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body("Tournament player size is insufficient to start");
        }
    }


    @CrossOrigin
    @PostMapping("/{id}/nextround")
    public ResponseEntity<?> nextRound(@PathVariable("id") Integer tournamentId) {
        try {
            List<UserAccount> winners = tournamentService.getWinners(tournamentId);
            if(winners.size() == 1){
                Tournament tournament = tournamentService.getTournament(tournamentId);
                tournament.setStatus(TournamentStatus.COMPLETED);
                tournamentRepository.save(tournament);
                return ResponseEntity.status(HttpStatus.OK).body("Tournament has ended");
            }
            Integer highestRound = tournamentService.getHighestRound(tournamentId);
            matchService.createMatches(winners, highestRound+1, tournamentId );
            return ResponseEntity.status(HttpStatus.OK).body("Next round has started");
        }
        catch (MatchesNotCompletedException e) {
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        }
        // catch (Exception e) {
        //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        //             .body("An error occurred while creating the next round");
        // }
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
        if (!tournamentRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tournament not found");
        }
        tournamentService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Tournament deleted successfully");
    }

/* End of DELETE Mappings */

}
