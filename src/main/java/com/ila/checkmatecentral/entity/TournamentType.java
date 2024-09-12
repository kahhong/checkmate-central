package com.ila.checkmatecentral.entity;

public enum TournamentType {

    SINGLE_KNOCKOUT("Single Knockout");

    private final String displayName;

    TournamentType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return  displayName;
    }
}
