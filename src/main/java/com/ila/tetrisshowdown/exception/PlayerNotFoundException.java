package com.ila.tetrisshowdown.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="player not found")
public class PlayerNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    
    public PlayerNotFoundException(String message) {
        super(message);
    }
}
