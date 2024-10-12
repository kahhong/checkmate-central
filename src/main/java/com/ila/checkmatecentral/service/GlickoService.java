package com.ila.checkmatecentral.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.ila.checkmatecentral.entity.Match;
import com.ila.checkmatecentral.entity.UserAccount;
import com.ila.checkmatecentral.repository.UserAccountRepository;
import com.ila.checkmatecentral.utility.GlickoCalculator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service

public class GlickoService {

    public final UserAccountRepository userAccountRepository;

    // Given a match object, it will retrieve both player and the score and update each player's Rating and RD accordingly.
    public void updateRatings(Match match) {
        UserAccount player1 = match.getPlayer1();
        UserAccount player2 = match.getPlayer2();

        player1 = inactivityAdjustment(player1);
        player2 = inactivityAdjustment(player2);

        double score = match.getOutcome();

        updatePlayerRating(player1, player2, score);
        updatePlayerRating(player2, player1, 1 - score);
    }

    public UserAccount inactivityAdjustment(UserAccount player) {
        long weeks = ChronoUnit.WEEKS.between(player.getTimeLastPlayed(), LocalDateTime.now());

        if (weeks > 2) {
            double adjustedRatingDeviation = GlickoCalculator.adjustRatingDeviationForInactivity(player.getRatingDeviation(), weeks);
            player.setRatingDeviation(adjustedRatingDeviation);
            userAccountRepository.save(player);
        }

        return player;
    }
    // for now it will just print out
    @Transactional
    public void updatePlayerRating(UserAccount player, UserAccount opponent, double score) {
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
        userAccountRepository.save(player);
    }
}
