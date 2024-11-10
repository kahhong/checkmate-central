package com.ila.checkmatecentral.service;

import com.ila.checkmatecentral.entity.Match;
import com.ila.checkmatecentral.entity.Player;
import com.ila.checkmatecentral.repository.PlayerRepository;
import com.ila.checkmatecentral.utility.GlickoCalculator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Service

public class GlickoService {

    public final PlayerRepository playerRepository;

    // Given a match object, it will retrieve both player and the score and update each player's Rating and RD accordingly.
    public void updateRatings(Match match) {
        Player player1 = match.getPlayer1();
        Player player2 = match.getPlayer2();

        player1 = inactivityAdjustment(player1);
        player2 = inactivityAdjustment(player2);
        Match.MatchOutcome outcome = match.getOutcome();

        if(outcome == Match.MatchOutcome.LOSE) {
            updatePlayerRating(player2, player1, 1);
            updatePlayerRating(player1, player2, 0);
        }
        else if(outcome == Match.MatchOutcome.WIN) {
            updatePlayerRating(player1, player2, 1);
            updatePlayerRating(player2, player1, 0);
        }

    }

    public Player inactivityAdjustment(Player player) {
        long weeks = ChronoUnit.WEEKS.between(player.getTimeLastPlayed(), LocalDateTime.now());

        if (weeks > 2) {
            double adjustedRatingDeviation = GlickoCalculator.adjustRatingDeviationForInactivity(player.getRatingDeviation(), weeks);
            player.setRatingDeviation(adjustedRatingDeviation);
            playerRepository.save(player);
        }

        return player;
    }
    // for now it will just print out
    @Transactional
    public void updatePlayerRating(Player player, Player opponent, double score) {
        double newRating = GlickoCalculator.calculateNewRating(
                player.getRating(),
                player.getRatingDeviation(),
                opponent.getRating(),
                opponent.getRatingDeviation(),
                score
        );

        double newRD = GlickoCalculator.calculateNewRD(
                player.getRating(),
                player.getRatingDeviation(),
                opponent.getRating(),
                opponent.getRatingDeviation(),
                score
        );


        player.setRating(newRating);
        player.setRatingDeviation(newRD);
        player.setTimeLastPlayed(LocalDateTime.now());
        playerRepository.save(player);
    }
}
