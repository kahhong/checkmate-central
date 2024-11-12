package com.ila.checkmatecentral.exceptions;

public class PlayerNotFoundException extends RuntimeException{

    public PlayerNotFoundException(Long playerId) {
        super("Could not find Player " + playerId);
    }

}
