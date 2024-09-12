package com.ila.checkmatecentral.form;

import com.ila.checkmatecentral.entity.TournamentType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.util.Date;

@Getter
@Setter
public class TournamentCreateForm {
    @Size(min=3, max=12)
    @NotEmpty(message="Player ID is necessary!")
    private String name;

    @NotEmpty(message="Password is necessary!")
    private String description;

    @NotEmpty(message="Confirmation of password is necessary!")
    @NumberFormat
    private int maxPlayers;

    @NotEmpty(message="Email is necessary!")
    @NumberFormat
    private int minElo;

    @NotEmpty
    private TournamentType type;

//    @NotEmpty
//    private MatchmakeAlgorithm matchmakeAlgorithm;

    @NotEmpty
    @DateTimeFormat
    private Date startDate;

    @NotEmpty
    @DateTimeFormat
    private Date endDate;
}
