package com.ila.checkmatecentral.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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

    private Date startDate;

    @Id
    @ManyToOne
    @JoinColumn(name="tournament_id", insertable = false, updatable = false)
    private Tournament tournament;
}