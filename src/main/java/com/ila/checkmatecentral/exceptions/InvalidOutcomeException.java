package com.ila.checkmatecentral.exceptions;

public class InvalidOutcomeException extends IllegalArgumentException {

    public InvalidOutcomeException(double outcome) {
        super("Outcome has to be value of 0.0, -1.0 or 1.0, not : " + outcome);
    }

}
