package com.ila.checkmatecentral.exceptions;

public class TournamentFullException extends InvalidTournamentStateException {
    public TournamentFullException(int tournamentId) {
        super("Tournament ID: " + tournamentId + " is Full, adding player unsuccessful");
    }
}
