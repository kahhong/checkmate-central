package com.ila.checkmatecentral.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@IdClass(MatchPk.class)
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int matchId;

    private MatchStatus matchStatus;

    private MatchType matchType;

    private LocalDateTime matchDateTime;

    private int round;

    // one player can enter multiple matches
    @ManyToOne
    @JoinColumn(name = "player1_id", referencedColumnName = "id")
    private UserAccount player1;

    @ManyToOne
    @JoinColumn(name = "player2_id", referencedColumnName = "id")
    private UserAccount player2;

    private double outcome;

    @Id
    @ManyToOne
    @JoinColumn(name="tournamentId", insertable = false, updatable = false, referencedColumnName = "tournamentId")
    private Tournament tournament;

    public Match(UserAccount player1, UserAccount player2, LocalDateTime dateTime, int round) {
        this.player1 = player1;
        this.player2 = player2;
        this.matchDateTime = dateTime;
        this.round = round;
        this.matchStatus = MatchStatus.ONGOING;
        // -1 = no result yet
        // 0 = player 1 lose
        // 0.5 = tie
        // 1 = player 2 lose
        this.outcome = -1;
    }
}