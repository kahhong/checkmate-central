package com.ila.checkmatecentral.exceptions;

public class TournamentFullException extends  RuntimeException{
    public TournamentFullException(Integer tournamentId) {
        super("Tournament ID: " + tournamentId + " is Full, adding player unsuccessful");
    }
}
