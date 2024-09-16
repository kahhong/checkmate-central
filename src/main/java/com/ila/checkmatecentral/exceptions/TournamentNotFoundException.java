package com.ila.checkmatecentral.exceptions;

public class TournamentNotFoundException extends RuntimeException{
    
    public TournamentNotFoundException(Integer tournamentId) {
        super("Could not find Tournament " + tournamentId);
    }
    
}
