package com.ila.checkmatecentral.exceptions;

public class TournamentNotFoundException extends RuntimeException{
    
    public TournamentNotFoundException(Long tournamentId) {
        super("Could not find Tournament " + tournamentId);
    }
    
}
