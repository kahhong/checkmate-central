package com.ila.checkmatecentral.controller;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.ila.checkmatecentral.exceptions.InvalidOutcomeException;
import com.ila.checkmatecentral.exceptions.MatchNotFoundException;
import com.ila.checkmatecentral.exceptions.TournamentNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ila.checkmatecentral.config.GlobalExceptionHandler;
import com.ila.checkmatecentral.entity.Match;
import com.ila.checkmatecentral.entity.Tournament;
import com.ila.checkmatecentral.service.MatchService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/match")
public class MatchController {
    public final MatchService matchService;

    // get every match from ONE tournament
    @GetMapping({"/list/{id}"})
    public ResponseEntity<?> getMatchesFromTournament(@PathVariable("id") Integer tournamentId) {
        try {
            List<Match> matchList = matchService.getMatches(tournamentId);
            return ResponseEntity.status(HttpStatus.OK).body(matchList);
        } catch (TournamentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping("/{id}/update")
    public ResponseEntity<?> updateMatchOutcome(@PathVariable("id") Integer matchId, @RequestBody JsonNode json) {
        double outcome = json.get("outcome").asDouble();
        try {
            Match updatedMatch = matchService.updateMatch(matchId, outcome);
            return ResponseEntity.status(HttpStatus.OK).body("Match " + matchId + " has been updated with " + outcome);
        } catch (MatchNotFoundException e) {
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        } catch (InvalidOutcomeException e) {
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the match.");
        }

    }

    // get all matches from all tournaments
    // @GetMapping({"/listall"})
    // public List<Match> getAllMatches() {
    //         return matchRepository.findAll();
    // }

    // get all matches from ONE player
    // @GetMapping({"/list"})
    // public ResponseEntity<List<Match>> getAllMatchs() {
    //     List<Match> Matchs = MatchService.getAllMatches();
    //     return ResponseEntity.ok(Matchs);
    // }

    // @GetMapping("/")
    // public Match getMatchById(@PathVariable("id") Integer id) {
    //     return matchRepository.findByMatchId(id)
    //         .orElseThrow(() -> new MatchNotFoundException(id));
    // }

    // @CrossOrigin
    // @PostMapping("/")
    // public void createMatches(List<UserAccount> userList, int round) {
    //     matchService.createMatches(userList, round);
    // }

    // @PutMapping("/{id}")
    // public ResponseEntity<?> updateMatch(@PathVariable("id") Integer MatchId,
    //         @RequestBody Match match, MatchStatus status) {

    //     return ResponseEntity.ok(Match);
    // }

    
}
