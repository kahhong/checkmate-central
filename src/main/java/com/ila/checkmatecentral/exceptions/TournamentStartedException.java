package com.ila.checkmatecentral.exceptions;

public class TournamentStartedException extends RuntimeException{
    public TournamentStartedException(Integer tournamentId) {
        super("Tournament ID: " + tournamentId + " has been started, Player added Unsuccessful");
    }
}
