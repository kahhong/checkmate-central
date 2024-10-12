package com.ila.checkmatecentral.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MatchPk implements Serializable {
    private Integer tournamentId;
    private int matchId;
}
