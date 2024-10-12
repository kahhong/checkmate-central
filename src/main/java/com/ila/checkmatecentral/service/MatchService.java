package com.ila.checkmatecentral.service;


import java.time.LocalDateTime;
import java.util.List;

import com.ila.checkmatecentral.entity.MatchStatus;
import com.ila.checkmatecentral.exceptions.InvalidOutcomeException;
import com.ila.checkmatecentral.exceptions.MatchNotFoundException;
import com.ila.checkmatecentral.exceptions.TournamentNotFoundException;
import com.ila.checkmatecentral.repository.TournamentRepository;
import com.ila.checkmatecentral.repository.UserAccountRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ila.checkmatecentral.entity.Match;
import com.ila.checkmatecentral.entity.UserAccount;

import com.ila.checkmatecentral.repository.MatchRepository;

import lombok.RequiredArgsConstructor;

@Slf4j
@RequiredArgsConstructor
@Service

public class MatchService {
    public final MatchRepository matchRepository;
    public final TournamentRepository tournamentRepository;
    public final GlickoService glickoService;


    public void createMatches(List<UserAccount> players, int round, Integer tournamentId) {
        // Sort players by ELO in descending order
        players.sort((p1, p2) -> Double.compare(p2.getRating(), p1.getRating()));
        
        int n = players.size();

        // Pair highest with lowest, second highest with second lowest, etc.
        for (int i = 0; i < n / 2; i++) {
            UserAccount player1 = players.get(i);
            UserAccount player2 = players.get(n - 1 - i);
            LocalDateTime dateTime = LocalDateTime.now();

            Match match = new Match(player1, player2, dateTime, round, tournamentId);

            matchRepository.save(match);
        }
    }

    public Match getMatch(Integer matchId) {
        return matchRepository.findByMatchIdWithoutPassword(matchId).orElseThrow(() -> new MatchNotFoundException(matchId));
    }

    public List<Match> getMatches(Integer tournamentId) {
        return matchRepository.findByTournamentId(tournamentId).orElseThrow(() -> new TournamentNotFoundException(tournamentId));
    }


    // update match - outcome
    public Match updateMatch(Integer matchId, double outcome) {
        //check if outcome is of valid input value
        if(outcome != 0.5 && outcome != 1 && outcome != 0) {
            throw new InvalidOutcomeException(outcome);
        }
        Match currentMatch = matchRepository.findByMatchId(matchId).orElseThrow(() -> new MatchNotFoundException(matchId));
        currentMatch.setOutcome(outcome);
        glickoService.updateRatings(currentMatch);
        currentMatch.setMatchStatus(MatchStatus.COMPLETED);
        return matchRepository.save(currentMatch);
    }

    // fetch winners from previous round
    /*
    public List<UserAccount> getWinners(Integer matchId, int round) {
        Match currentMatch = matchRepository.findByMatchId(matchId).orElseThrow(() -> new MatchNotFoundException(matchId));
        currentMatch.setOutcome(outcome);
        currentMatch.setMatchStatus(MatchStatus.COMPLETED);
        return matchRepository.save(currentMatch);
    }
    */

}
