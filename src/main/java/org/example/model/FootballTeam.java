package org.example.model;

public class FootballTeam extends Team {
    private final String teamName;
    private final String shortName;

    public FootballTeam(String teamName, String shortName) {
        super("Football", 11);

        this.teamName = teamName;
        this.shortName = shortName;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getShortName() {
        return shortName;
    }
}