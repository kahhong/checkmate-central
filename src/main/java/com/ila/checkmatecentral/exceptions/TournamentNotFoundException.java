package com.ila.checkmatecentral.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error
public class TournamentNotFoundException extends RuntimeException{
    
    TournamentNotFoundException(Long tournamentId) {
        super("Could not find Tournament " + tournamentId);
    }
    
}
