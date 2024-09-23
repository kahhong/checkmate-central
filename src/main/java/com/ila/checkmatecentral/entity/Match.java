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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int matchId;

    @Id
    private Integer tournamentId;

    private MatchStatus matchStatus;

    private MatchType matchType;

    private LocalDateTime matchDateTime;

    private int round;

    // one player can enter multiple matches
    @ManyToOne
    @JoinColumn(name = "player1Id", referencedColumnName = "id")
    private UserAccount player1;

    @ManyToOne
    @JoinColumn(name = "player2Id", referencedColumnName = "id")
    private UserAccount player2;

    private double outcome;

    @ManyToOne
    @JoinColumn(name="tournamentId", insertable = false, updatable = false)
    private Tournament tournament;


    public Match(UserAccount player1, UserAccount player2, LocalDateTime dateTime, int round, Integer tournamentId) {
        this.player1 = player1;
        this.player2 = player2;
        this.matchDateTime = dateTime;
        this.round = round;
        this.matchStatus = MatchStatus.ONGOING;
        this.tournamentId = tournamentId;
        // -1 = no result yet
        // 0 = player 1 lose
        // 0.5 = tie
        // 1 = player 2 lose
        this.outcome = -1;
    }

    public Match() {}

    @Override
    public String toString() {
        return "Match{" +
                "matchId=" + matchId +
                ", tournamentId=" + tournamentId +
                ", matchStatus=" + matchStatus +
                ", matchType=" + matchType +
                ", matchDateTime=" + matchDateTime +
                ", round=" + round +
                ", player1=" + player1 +
                ", player2=" + player2 +
                ", outcome=" + outcome +
                ", tournament=" + tournament +
                '}';
    }
}