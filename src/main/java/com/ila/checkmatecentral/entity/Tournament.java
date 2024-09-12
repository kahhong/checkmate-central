package com.ila.checkmatecentral.entity;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
