package org.example.model;

public class Team {
    private final String sport;
    private final int playerNumber;

    public Team(String sport, int playerNumber) {
        this.sport = sport;
        this.playerNumber = playerNumber;
    }

    public String getSport() {
        return sport;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}