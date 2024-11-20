package com.ila.checkmatecentral.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ila.checkmatecentral.entity.Match;
import com.ila.checkmatecentral.entity.MatchPk;
import com.ila.checkmatecentral.entity.Player;
import com.ila.checkmatecentral.entity.Tournament;

public interface MatchRepository extends JpaRepository<Match, MatchPk> {


    Optional<Match> findByMatchId(int matchId);


    Optional<List<Match>> findByTournamentId(Integer tournamentId);

    default Optional<Match> findByMatchIdWithoutPassword(int matchId) {
        return findByMatchId(matchId).map(match -> {
            if (match == null) {
                return null;
            }

            Player player1 = match.getPlayer1();
            Player player2 = match.getPlayer2();

            Tournament tournament = match.getTournament();

            List<Player> u1 = tournament.getPlayerList();
            for(Player u : u1){
                u.eraseCredentials();
            }
            player1.eraseCredentials();
            player2.eraseCredentials();

            return match;
        });
    }
}
