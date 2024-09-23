package com.ila.checkmatecentral.entity;

public enum MatchStatus {
    ONGOING("Ongoing"),

    COMPLETED("Completed");


    private final String displayName;

    MatchStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return  displayName;
    }
}
