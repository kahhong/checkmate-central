package com.ila.checkmatecentral.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


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

    @Enumerated(EnumType.STRING)
    private MatchOutcome outcome;

    @ManyToOne
    @JoinColumn(name="tournamentId", insertable = false, updatable = false)
    private Tournament tournament;

    public enum MatchOutcome {
        WIN, LOSE, DRAW, NO_RESULT
    }
    public Match(UserAccount player1, UserAccount player2, LocalDateTime dateTime, int round, Integer tournamentId) {
        this.player1 = player1;
        this.player2 = player2;
        this.matchDateTime = dateTime;
        this.round = round;
        this.matchStatus = MatchStatus.ONGOING;
        this.matchType = MatchType.ONE_VS_ONE;
        this.tournamentId = tournamentId;
        this.outcome = MatchOutcome.NO_RESULT;
    }

    protected Match() {}

    public UserAccount getWinner() {
        if (outcome == MatchOutcome.WIN) {
            return player1;

        } else if (outcome == MatchOutcome.LOSE) {
            return player2;
        }
        // no draw in single knockout tournament -> assume draw, players will fight again until outcome is determined
        return null;
    }

    public UserAccount getLoser() {
        if (outcome == MatchOutcome.WIN) {
            return player2;

        } else if (outcome == MatchOutcome.LOSE) {
            return player1;
        }
        // no draw in single knockout tournament -> assume draw, players will fight again until outcome is determined
        return null;
    }

    public MatchStatus getMatchStatus() {
        return matchStatus;
    }

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