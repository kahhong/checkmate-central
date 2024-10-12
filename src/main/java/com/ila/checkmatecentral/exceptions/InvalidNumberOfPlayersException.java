package com.ila.checkmatecentral.exceptions;

public class InvalidNumberOfPlayersException extends IllegalArgumentException {

    public InvalidNumberOfPlayersException() {
        super("Number of players must be power of 2");
    }

}
