package com.ila.checkmatecentral.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ila.checkmatecentral.entity.Tournament;

public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
}
