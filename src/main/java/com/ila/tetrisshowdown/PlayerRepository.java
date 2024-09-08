package com.ila.tetrisshowdown;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Optional<Player> findByEmail(String email);
}
