package com.ila.checkmatecentral.repository;

import com.ila.checkmatecentral.entity.Match;
import com.ila.checkmatecentral.entity.MatchPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, MatchPk> {
}
