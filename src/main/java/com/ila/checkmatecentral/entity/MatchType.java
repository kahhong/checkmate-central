package com.ila.checkmatecentral.entity;

public enum MatchType {
    ONE_VS_ONE("One Vs One");


    private final String displayName;

    MatchType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return  displayName;
    }
}
