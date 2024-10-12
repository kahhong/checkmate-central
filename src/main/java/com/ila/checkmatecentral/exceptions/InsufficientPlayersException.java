package com.ila.checkmatecentral.exceptions;

public class InsufficientPlayersException extends RuntimeException {
    public InsufficientPlayersException(Integer currentNumberOfPlayers, Integer RequiredNumberOfPlayers) {
        super("The current number of player in the tournament is insufficient to start the tournament " +
                "- Current Players: " + currentNumberOfPlayers + ", Required Players: " + RequiredNumberOfPlayers);
    }
}
