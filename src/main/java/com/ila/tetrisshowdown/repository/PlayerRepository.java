package com.ila.tetrisshowdown.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ila.tetrisshowdown.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Optional<Player> findByEmail(String email);
}
