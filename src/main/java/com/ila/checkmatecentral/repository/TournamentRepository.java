package com.ila.checkmatecentral.repository;

import com.ila.checkmatecentral.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TournamentRepository extends JpaRepository<Player, Integer> {
    Optional<Player> findById(int id);
}
