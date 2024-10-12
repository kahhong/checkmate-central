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
import com.ila.checkmatecentral.exceptions.MatchNotFoundException;
import com.ila.checkmatecentral.service.MatchService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/match")
public class MatchController {
    public final MatchService matchService;

    @GetMapping({"/list/{id}"})
    public List<Match> getMatchesFromTournament(@PathVariable("id") Integer tournamentId) {
        return matchService.getMatches(tournamentId);
    }

    @CrossOrigin
    @PostMapping("/{id}/update")
    public ResponseEntity<?> updateMatchOutcome(@PathVariable("id") Integer matchId, @RequestBody JsonNode json) {
        double outcome = json.get("outcome").asDouble();

        if (outcome != 0.5 && outcome != 1 && outcome != 0) {
            return ResponseEntity.status(HttpStatus.OK).body("Invalid outcome");
        }

        try {
            matchService.updateMatchOutcome(matchId, outcome);
            return ResponseEntity.status(HttpStatus.OK).body("Match " + matchId + " has been updated with " + outcome);

        } catch (MatchNotFoundException e) {
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the match.");
        }
    }
}
