package com.ila.checkmatecentral.repository;

import com.ila.checkmatecentral.entity.Player;
import com.ila.checkmatecentral.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
    Optional<Tournament> findById(int id);
}
