package com.ila.checkmatecentral.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Tournament {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tournamentId;

    @OneToMany(mappedBy = "tournament")
    private Collection<Match> matches;

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
    }
}
