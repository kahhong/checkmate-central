package com.ila.checkmatecentral.exceptions;

public class InvalidTournamentStateException extends RuntimeException {
    public InvalidTournamentStateException(String message) {
        super(message);
    }
}
