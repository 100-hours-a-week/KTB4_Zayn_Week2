package org.example.model;

import org.example.common.ErrorMessage;

public class TournamentParticipant implements Winnable, Injurable, Scorable {
    private final FootballTeam team;
    private final double baseWinningRate;
    private double winningRate;
    private int occurInjuryCount = 0;
    private int score = 0;

    public TournamentParticipant(FootballTeam team, double winningRate) {
        this.team = team;
        this.baseWinningRate = winningRate;
        this.winningRate = winningRate;
    }

    public String getTeamName() {
        return team.getTeamName();
    }

    public String getShortName() {
        return team.getShortName();
    }

    public void setWinningRate(double rate) {
        if (rate <= 0)
            throw new IllegalArgumentException(ErrorMessage.INVALID_PARAMETER.getMessage());

        this.winningRate = rate;
    }

    public double getWinningRate() {
        return this.winningRate;
    }

    public double getBaseWinningRate() {
        return this.baseWinningRate;
    }

    public void injure() {
        this.occurInjuryCount++;
    }

    public int getInjuryCount() {
        return this.occurInjuryCount;
    }

    public void clearInjuryCount() {
        this.occurInjuryCount = 0;
    }

    public double getDefenseAbility() {
        return team.getDefenseAbility();
    }

    public void resetScore() {
        this.score = 0;
    }

    public void addScore() {
        this.score++;
    }

    public int getScore() {
        return this.score;
    }
}
