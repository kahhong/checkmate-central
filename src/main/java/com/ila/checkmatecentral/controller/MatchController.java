package com.ila.checkmatecentral.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.ila.checkmatecentral.entity.Match;
import com.ila.checkmatecentral.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/match")
@CrossOrigin
public class MatchController {
    public final MatchService matchService;

    // get every match from ONE tournament
    @GetMapping({ "/{matchId}" })
    public ResponseEntity<?> getMatch(@PathVariable("matchId") Integer matchId) {
        Match match = matchService.getMatch(matchId);
        return ResponseEntity.status(HttpStatus.OK).body(match);
    }

    // get every match from ONE tournament
    @GetMapping({ "/list/{tournamentId}" })
    public ResponseEntity<?> getMatchesFromTournament(@PathVariable("tournamentId") Integer tournamentId) {
        List<Match> matches = matchService.getMatches(tournamentId);
        return ResponseEntity.status(HttpStatus.OK).body(matches);
    }

    @CrossOrigin
    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateMatchOutcome(@PathVariable("id") Integer matchId, @RequestBody JsonNode json) {
        String outcomeText = json.get("outcome").asText();
        Match.MatchOutcome outcome = Match.MatchOutcome.valueOf(outcomeText);

        matchService.updateMatchOutcome(matchId, outcome);
        return ResponseEntity.status(HttpStatus.OK).body(
            Map.of("message", "Match " + matchId + " has been updated with " + outcome));
    }
}
