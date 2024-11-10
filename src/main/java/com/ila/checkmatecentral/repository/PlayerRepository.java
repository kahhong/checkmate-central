package com.ila.checkmatecentral.repository;

import java.util.Optional;

import com.ila.checkmatecentral.entity.AccountCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ila.checkmatecentral.entity.Player;
import org.springframework.lang.NonNull;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    @NonNull
    Optional<Player> findById(@NonNull Long id);
    Optional<AccountCredential> findByEmail(String email);
}
