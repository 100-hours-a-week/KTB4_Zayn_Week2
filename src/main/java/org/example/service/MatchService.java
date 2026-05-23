package org.example.service;

import org.example.common.FootballConstant;
import org.example.common.RandomConstant;
//import org.example.model.FootballTeam;
import org.example.model.Injurable;
import org.example.model.TournamentParticipant;
import org.example.model.Winnable;

import java.util.Random;
import java.util.stream.Stream;

public class MatchService {
    private final Random rd;

    public MatchService() {
        this.rd = new Random();
    }

    // 과중책임 -> 추후 메서드 분리하고 특정 상수는 enum으로 관리
    public TournamentParticipant fight(TournamentParticipant teamA, TournamentParticipant teamB) {
        int matchTime = 0;

        teamA.resetScore();
        teamB.resetScore();

        teamA.clearInjuryCount();
        teamB.clearInjuryCount();

        teamA.setWinningRate(teamA.getBaseWinningRate());
        teamB.setWinningRate(teamB.getBaseWinningRate());

        while (matchTime < FootballConstant.FULL_TIME.getValue()) {
            // 수비팀 선택
            Winnable defender = determineDefender(teamA, teamB);

            // 수비 실패
            if (!tryDefense(defender)) {
                if (defender == teamB) teamA.addScore();
                if (defender == teamA) teamB.addScore();
            }

            // 부상 로직 적용 -> 공격-수비 팀 선택에 영향을 끼친다.
            applyInjuries(teamA, teamB);

            // 경기 시간 증가
            matchTime += FootballConstant.TIME_SPLIT.getValue();
        }

        if (teamA.getScore() == teamB.getScore())
            return penaltyShootout() ? teamA : teamB;

        return teamA.getScore() > teamB.getScore() ? teamA : teamB;
    }

    private Winnable determineDefender(Winnable teamA, Winnable teamB) {
        double teamAWeight = Math.sqrt(
                teamA.getWinningRate()
        );

        double teamBWeight = Math.sqrt(
                teamB.getWinningRate()
        );

        double r = rd.nextDouble() * (teamAWeight + teamBWeight);

        if (teamAWeight >= r) return teamB;
        return teamA;
    }

    private boolean tryDefense(Winnable defender) {
        double defenseAbility = ((TournamentParticipant) defender).getDefenseAbility();

        double defenseProbability = RandomConstant.DEFENSE_BASE.getValue()
                + (defenseAbility * RandomConstant.DEFENSE_SCALE.getValue());

        return rd.nextDouble() < defenseProbability; // true: 수비성공
    }

    private boolean penaltyShootout() {
        int shootoutTime = rd.nextInt(FootballConstant.SHOOTOUT_TIME.getValue());
        try {
            Thread.sleep(shootoutTime);
        } catch (InterruptedException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return rd.nextDouble() > 0.5;
    }

    private void applyInjuries(Winnable teamA, Winnable teamB) {
        Stream.of(teamA, teamB)
                .filter(Injurable.class::isInstance)
                .map(Injurable.class::cast)
                .forEach(this::injuryOccur);
    }

    private void injuryOccur(Injurable team) {
        double r = rd.nextDouble();
        if (r < RandomConstant.INJURY_PROBABILITY.getValue()) {
            team.injure();
            decreaseWinningRate((Winnable) team);
        }
    }

    private void decreaseWinningRate(Winnable team) {
        if (!(team instanceof Injurable)) return;

        team.setWinningRate(
                team.getWinningRate() * Math.pow(
                        RandomConstant.DECREASE_RATE.getValue(),
                        ((Injurable) team).getInjuryCount()
                )
        );
    }
}