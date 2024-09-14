package com.ila.tetrisshowdown.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Tournament not found")
public class TournamentNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    
    public TournamentNotFoundException(String message) {
        super(message);
    }
}

