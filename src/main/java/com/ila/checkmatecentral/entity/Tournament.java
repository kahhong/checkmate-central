package com.ila.checkmatecentral.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Tournament {
    @Id
    @Column(unique = true)
    private int tournamentId;

    private String tournamentName;

    private String tournamentDescription;

    private TournamentStatus status;

    private TournamentType type;

    private int maxPlayers;

    private int minElo;

    private Date startDate;

    private Date endDate;
}
