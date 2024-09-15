package com.ila.checkmatecentral.exceptions;

public class TournamentNotFoundException extends RuntimeException{
    
    TournamentNotFoundException(Long tournamentId) {
        super("Could not find Tournament " + tournamentId);
    }
    
}
