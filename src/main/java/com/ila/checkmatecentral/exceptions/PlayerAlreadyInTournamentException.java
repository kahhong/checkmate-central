package com.ila.checkmatecentral.exceptions;

public class PlayerAlreadyInTournamentException extends RuntimeException {
    public PlayerAlreadyInTournamentException(Integer tournamentId) {
        super("Player is already registered in tournament with ID: " + tournamentId);
    }
}
