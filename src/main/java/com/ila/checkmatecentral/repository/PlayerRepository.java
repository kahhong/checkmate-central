package com.ila.checkmatecentral.repository;

import java.util.Optional;

import com.ila.checkmatecentral.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, String> {
    Optional<Player> findByEmail(String email);
}
