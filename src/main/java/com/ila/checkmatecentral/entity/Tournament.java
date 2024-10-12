package com.ila.checkmatecentral.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

@Entity
@Slf4j
public class Tournament {
    // Start of server generated fields

    @Getter
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tournamentId;

    @Getter
    @Setter
    private TournamentStatus status;

    @Getter
    @Setter
    private LocalDateTime createDate;

    @Getter
    @Setter
    private int round;


    @OneToMany(mappedBy = "tournament")
    private List<Match> matches;

    @Getter
    @Setter
    @OneToMany(mappedBy = "tournament")
    @JsonManagedReference
    private List<UserAccount> playerList;

    // End of server generated fields



    /* Sample JSON from POST /tournaments:
     *      {
     *          "name" : "Baby's First Steps",
     *          "description: "Chess tournament for infants",
     *          "type": "SINGLE KNOCKOUT",
     *          "maxPlayers: "10",
     *          "minElo": "1000",
     *          "startDate": "2024-10-01T15:35:41Z"     //ISO DateTime Format
     *          "endDate": "2024-11-01T15:35:41Z"       //ISO DateTime Format
     *      }
     */

    // Start of user input fields

    @Getter
    @Setter
    @JsonProperty("name")
    @NotEmpty(message="Tournament name is required!")
    private String name;

    @Getter
    @Setter
    @JsonProperty("description")
    @NotEmpty(message="Description is required!")
    private String description;

    @Getter
    @Setter
    @JsonProperty("type")
    @NotNull
    private TournamentType type;

    @Getter
    @Setter
    @JsonProperty("maxPlayers")
    @Range(min=2, max=100, message = "Number of maximum players must be between 2 and 100.")
    @NumberFormat
    private int maxPlayers;

    @Getter
    @Setter
    @JsonProperty("minElo")
    @PositiveOrZero(message = "Elo must be a positive number.")
    @NumberFormat
    private int minElo;

    @Getter
    @Setter
    @JsonProperty("startDate")
    @NotNull(message = "Start date is required")
    @DateTimeFormat
    @FutureOrPresent(message = "Start date is in the past.")
    private Date startDate;

    @Getter
    @Setter
    @JsonProperty("endDate")
    @NotNull(message = "End date is required")
    @DateTimeFormat
    private Date endDate;

     // End of user input fields


    public void addPlayer(UserAccount player){
        this.playerList.add(player);
        player.setTournament(this);
    }
}
