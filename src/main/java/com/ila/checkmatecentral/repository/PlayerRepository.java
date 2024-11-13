package com.ila.checkmatecentral.repository;

import com.ila.checkmatecentral.entity.AccountCredential;
import com.ila.checkmatecentral.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    @NonNull
    Optional<Player> findById(@NonNull Long id);
    Optional<AccountCredential> findByEmail(String email);
    long countByRatingGreaterThan(double rating);
}
