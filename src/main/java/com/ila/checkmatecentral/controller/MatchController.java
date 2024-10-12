package com.ila.checkmatecentral.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.ila.checkmatecentral.entity.Match;
import com.ila.checkmatecentral.exceptions.InvalidOutcomeException;
import com.ila.checkmatecentral.exceptions.MatchNotFoundException;
import com.ila.checkmatecentral.exceptions.TournamentNotFoundException;
import com.ila.checkmatecentral.service.MatchService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/match")
public class MatchController {
    public final MatchService matchService;

    // get every match from ONE tournament
    @GetMapping({"/{matchId}"})
    public ResponseEntity<?> getMatch(@PathVariable("matchId") Integer matchId) {
        try {
            Match match = matchService.getMatch(matchId);
            return ResponseEntity.status(HttpStatus.OK).body(match);
        } catch (TournamentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        }
    }

    // get every match from ONE tournament
    @GetMapping({"/list/{tournamentId}"})
    public ResponseEntity<?> getMatchesFromTournament(@PathVariable("tournamentId") Integer tournamentId) {
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
            matchService.updateMatchOutcome(matchId, outcome);
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
}
