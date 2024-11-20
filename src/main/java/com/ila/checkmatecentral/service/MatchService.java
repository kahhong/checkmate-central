package com.ila.checkmatecentral.service;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ila.checkmatecentral.entity.Match;
import com.ila.checkmatecentral.entity.MatchStatus;
import com.ila.checkmatecentral.entity.Player;
import com.ila.checkmatecentral.entity.Match.MatchOutcome;
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

    public void createSingleMatch(Player player1, Player player2, int round, int tournamentId) {
        Match match = new Match(player1, player2, LocalDateTime.now(), round, tournamentId);
        matchRepository.save(match);
    }

    public Match getMatch(Integer matchId) {
        return matchRepository.findByMatchIdWithoutPassword(matchId).orElseThrow(() -> new MatchNotFoundException(matchId));
    }

    public List<Match> getMatches(int tournamentId) {
        return matchRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
    }
    
    public Match withdrawPlayer(Match match, Player player) {
        if (match.getPlayer1().equals(player)) {
            return updateMatchOutcome(match, MatchOutcome.LOSE);
        } else if (match.getPlayer2().equals(player)) {
            return updateMatchOutcome(match, MatchOutcome.WIN);
        } else {
            return null;
        }
    }

    public Match updateMatchOutcome(Integer matchId, Match.MatchOutcome outcome) {
        Match match = matchRepository.findByMatchId(matchId).orElseThrow(() -> new MatchNotFoundException(matchId));
        return updateMatchOutcome(match, outcome);
    }

    public Match updateMatchOutcome(Match match, Match.MatchOutcome outcome) {
        match.setOutcome(outcome);
        glickoService.updateRatings(match);
        match.setMatchStatus(MatchStatus.COMPLETED);
        return matchRepository.save(match);
    }
}
