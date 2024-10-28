package com.ila.checkmatecentral.exceptions;

public class InvalidNumberOfPlayersException extends IllegalArgumentException {

    public InvalidNumberOfPlayersException(int numPlayers) {
        super("Number of players must be power of 2 but number of players is " + numPlayers);
    }

}
