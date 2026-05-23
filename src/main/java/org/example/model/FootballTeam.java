package org.example.model;

public class FootballTeam extends Team {
    private final String teamName;
    private final String shortName;
    private final double defenseAbility;

    public FootballTeam(String teamName, String shortName, double defenseAbility) {
        super("Football", 11);

        this.teamName = teamName;
        this.shortName = shortName;
        this.defenseAbility = defenseAbility;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getShortName() {
        return shortName;
    }

    public double getDefenseAbility() {
        return defenseAbility;
    }
}