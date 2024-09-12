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
    private int matchId;

    @Id
    private int tournamentId;

    private MatchStatus matchStatus;

    private MatchType matchType;

    private Date startDate;
}