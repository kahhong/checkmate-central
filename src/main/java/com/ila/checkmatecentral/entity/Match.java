package com.ila.checkmatecentral.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@IdClass(MatchPk.class)
public class Match {
    @Id
    private String email;

    @Id
    private String playerName;

    private MatchStatus matchStatus;

    private MatchType matchType;

    private Date startDate;
}