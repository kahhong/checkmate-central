package com.ila.checkmatecentral.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@Entity
public class Tournament {
    @Id
    @Column(unique = true)
    private int tournamentId;

    @OneToMany
    private Collection<Match> matches;

    private String name;

    private String description;

    private TournamentStatus status;

    private TournamentType type;

    private int maxPlayers;

    private int minElo;

    private Date startDate;

    private Date endDate;
}
