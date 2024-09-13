package com.ila.checkmatecentral.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PlayerAlreadyInTournamentException extends RuntimeException {
    public PlayerAlreadyInTournamentException(Long tournamentId) {
        super("Player is already registered in tournament with ID: " + tournamentId);
    }
}
