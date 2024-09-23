package com.ila.checkmatecentral.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ila.checkmatecentral.entity.Match;
import com.ila.checkmatecentral.repository.MatchRepository;
import com.ila.checkmatecentral.repository.TournamentRepository;
import com.ila.checkmatecentral.service.MatchService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/match")
public class MatchController {
    public final MatchService matchService;
    public final MatchRepository matchRepository;
    public final TournamentRepository tournamentRepository;

    // get every match from ONE tournament
    @GetMapping({"/list/{id}"})
    public List<Match> getMatchesFromTournament(@PathVariable("id") Integer tournamentId) {
        return matchRepository.findByTournamentId(tournamentId);
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
