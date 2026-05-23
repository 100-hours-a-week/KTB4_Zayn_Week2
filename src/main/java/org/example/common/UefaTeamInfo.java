package org.example.common;

public enum UefaTeamInfo {
    ARSENAL("Arsenal", "ARS", 27.4, 0.92),
    BAYERN("Bayern München", "FCB", 14.3, 0.84),
    LIVERPOOL("Liverpool", "LIV", 12.8, 0.86),
    MANCITY("Manchester City", "MCI", 10.8, 0.82),
    BARCELONA("Barcelona", "BAR", 7.7, 0.78),
    CHELSEA("Chelsea", "CHE", 6.9, 0.79),
    NEWCASTLE("Newcastle United", "NEW", 4.7, 0.76),
    PARIS("Paris Saint-Germain", "PSG", 4.6, 0.74),
    REALMADRID("Real Madrid", "RMA", 2.8, 0.75),
    SPORTING("Sporting CP", "SCP", 2.7, 0.73),
    ATLETICO("Atlético de Madrid", "ATM", 2, 0.88),
    TOTTENHAM("Tottenham Hotspur", "TOT", 1.2, 0.68),
    ATALANTA("Atalanta", "ATA", 1.1, 0.72),
    LEVERKUSEN("Bayer Leverkusen", "B04", 0.5, 0.71),
    GLIMT("Bodø/Glimt", "BOD", 0.4, 0.66),
    GALATASARAY("Galatasaray", "GAL", 0.2, 0.64);

    private final String fullName;
    private final String shortName;
    private final double winningRate;
    private final double defenseAbility;

    UefaTeamInfo(String fullName, String shortName, double winningRate, double defenseAbility) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.winningRate = winningRate;
        this.defenseAbility = defenseAbility;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getShortName() {
        return this.shortName;
    }

    public double getWinningRate() {
        return this.winningRate;
    }

    public double getDefenseAbility() {
        return defenseAbility;
    }
}