package com.ila.checkmatecentral.service;


import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ila.checkmatecentral.entity.Match;
import com.ila.checkmatecentral.entity.MatchStatus;
import com.ila.checkmatecentral.entity.UserAccount;
import com.ila.checkmatecentral.exceptions.InvalidOutcomeException;
import com.ila.checkmatecentral.exceptions.MatchNotFoundException;
import com.ila.checkmatecentral.exceptions.TournamentNotFoundException;
import com.ila.checkmatecentral.repository.MatchRepository;
import com.ila.checkmatecentral.repository.TournamentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchService {
    public final MatchRepository matchRepository;
    public final TournamentRepository tournamentRepository;
    public final GlickoService glickoService;

    public void createMatches(List<UserAccount> players, int round, int tournamentId) {
        // Sort players by ELO in descending order
        UserAccount[] sortedPlayers = players.toArray(UserAccount[]::new);
        Arrays.sort(sortedPlayers, (p1, p2) -> Double.compare(p2.getRating(), p1.getRating()));
        
        int n = sortedPlayers.length;

        // Pair highest with lowest, second highest with second lowest, etc.
        for (int i = 0; i < n / 2; i++) {
            UserAccount player1 = sortedPlayers[i];
            UserAccount player2 = sortedPlayers[n - 1 - i];

            Match match = new Match(player1, player2, new Date(), round, tournamentId);

            matchRepository.save(match);
        }
    }

    public Match getMatch(Integer matchId) {
        return matchRepository.findByMatchIdWithoutPassword(matchId).orElseThrow(() -> new MatchNotFoundException(matchId));
    }

    public List<Match> getMatches(int tournamentId) {
        return matchRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
    }

    public Match updateMatchOutcome(Integer matchId, double outcome) {
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
}
