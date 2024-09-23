package com.ila.checkmatecentral.exceptions;

public class MatchNotFoundException extends RuntimeException{

    public MatchNotFoundException(Integer matchId) {
        super("Could not find Match " + matchId);
    }

}
