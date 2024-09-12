package com.ila.checkmatecentral.entity;

public enum TournamentStatus {
    UPCOMING("Upcoming"),

    ONGOING("Ongoing"),

    CALCULATING("Calculating"),

    COMPLETED("Completed");


    private final String displayName;

    TournamentStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return  displayName;
    }
}
