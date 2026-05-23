package org.example.model;

import org.example.common.ErrorMessage;

public class TournamentParticipant implements Winnable, Injurable {
    private final FootballTeam team;
    private double winningRate;
    private int occurInjuryCount = 0;

    public TournamentParticipant(FootballTeam team, double winningRate) {
        this.team = team;
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

    public void injure() {
        this.occurInjuryCount++;
    }

    public int getInjuryCount() {
        return this.occurInjuryCount;
    }
}
