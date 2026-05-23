package org.example.common;

public enum UefaTeamInfo {
    ARSENAL("Arsenal", "ARS", 27.4),
    BAYERN("Bayern München", "FCB", 14.3),
    LIVERPOOL("Liverpool", "LIV", 12.8),
    MANCITY("Manchester City", "MCI", 10.8),
    BARCELONA("Barcelona", "BAR", 7.7),
    CHELSEA("Chelsea", "CHE", 6.9),
    NEWCASTLE("Newcastle United", "NEW", 4.7),
    PARIS("Paris Saint-Germain", "PSG", 4.6),
    REALMADRID("Real Madrid", "RMA", 2.8),
    SPORTING("Sporting CP", "SCP", 2.7),
    ATLETICO("Atlético de Madrid", "ATM", 2),
    TOTTENHAM("Tottenham Hotspur", "TOT", 1.2),
    ATALANTA("Atalanta", "ATA", 1.1),
    LEVERKUSEN("Bayer Leverkusen", "B04", 0.5),
    GLIMT("Bodø/Glimt", "BOD", 0.4),
    GALATASARAY("Galatasaray", "GAL", 0.2);

    private final String fullName;
    private final String shortName;
    private final double winningRate;

    UefaTeamInfo(String fullName, String shortName, double winningRate) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.winningRate = winningRate;
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
}