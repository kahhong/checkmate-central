package com.ila.checkmatecentral.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MatchPk implements Serializable {
    private Integer tournamentId;
    private int matchId;
}
