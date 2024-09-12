package com.ila.checkmatecentral.form;

import com.ila.checkmatecentral.entity.TournamentType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class TournamentCreateForm {
    @Size(min=3, max=12)
    @NotEmpty(message="Player ID is necessary!")
    private String name;

    @NotEmpty(message="Password is necessary!")
    private String description;

    @NotNull(message="Confirmation of password is necessary!")
    @NumberFormat
    private int maxPlayers;

    @NotNull(message="Email is necessary!")
    @NumberFormat
    private int minElo;

    @NotNull
    private TournamentType type;

//    @NotEmpty
//    private MatchmakeAlgorithm matchmakeAlgorithm;

    @NotNull
    @DateTimeFormat
    private Date startDate;

    @NotNull
    @DateTimeFormat
    private Date endDate;
}
