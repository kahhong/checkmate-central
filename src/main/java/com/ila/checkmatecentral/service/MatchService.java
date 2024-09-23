package com.ila.checkmatecentral.service;


import java.time.LocalDateTime;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
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
}
