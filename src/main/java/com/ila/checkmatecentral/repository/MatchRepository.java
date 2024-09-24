package com.ila.checkmatecentral.repository;

import com.ila.checkmatecentral.entity.Match;
import com.ila.checkmatecentral.entity.MatchPk;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, MatchPk> {
    Optional<Match> findByMatchId(Integer id);
    Optional<List<Match>> findByTournamentId(Integer tournamentId);
}
