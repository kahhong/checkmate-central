package com.ila.checkmatecentral.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MatchPk implements Serializable {
    private Integer tournamentId;
    private int matchId;
}
