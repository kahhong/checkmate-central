package com.ila.checkmatecentral.exceptions;

public class PlayerNotInTournamentException extends RuntimeException {
    public PlayerNotInTournamentException(Integer tournamentId) {
        super("Player is not registered in tournament ID: " + tournamentId);
    }
}
