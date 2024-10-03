package com.ila.checkmatecentral.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Entity
@Slf4j
public class Tournament {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tournamentId;

    @OneToMany(mappedBy = "tournament")
    private List<Match> matches;

    @OneToMany(mappedBy = "tournament")
    private List<UserAccount> playerList;

    private String name;

    private String description;

    private TournamentStatus status;

    private TournamentType type;

    private int maxPlayers;

    private int minElo;

    private Date startDate;

    private Date endDate;

    private int round;

    private LocalDateTime createDate;


    public void addPlayer(UserAccount player){
        this.playerList.add(player);
        player.setTournament(this);
    }

}
