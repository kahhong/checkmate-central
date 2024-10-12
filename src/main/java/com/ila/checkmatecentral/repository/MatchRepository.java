package com.ila.checkmatecentral.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ila.checkmatecentral.entity.Match;
import com.ila.checkmatecentral.entity.MatchPk;
import com.ila.checkmatecentral.entity.Tournament;
import com.ila.checkmatecentral.entity.UserAccount;

public interface MatchRepository extends JpaRepository<Match, MatchPk> {


    Optional<Match> findByMatchId(int matchId);


    Optional<List<Match>> findByTournamentId(Integer tournamentId);

    default Optional<Match> findByMatchIdWithoutPassword(int matchId){
        Match match =  findByMatchId(matchId).orElseThrow();
        UserAccount player1 = match.getPlayer1();
        UserAccount player2 = match.getPlayer2();

        Tournament tournament = match.getTournament();

        List<UserAccount> u1 = tournament.getPlayerList();
        for(UserAccount u : u1){
            u.eraseCredentials();
        }
        player1.eraseCredentials();
        player2.eraseCredentials();
        return Optional.of(match);
    }
}
