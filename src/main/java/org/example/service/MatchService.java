package org.example.service;

import org.example.common.FootballConstant;
import org.example.common.RandomConstant;
//import org.example.model.FootballTeam;
import org.example.common.TournamentConstant;
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

    public TournamentParticipant threadFight(Winnable teamA, Winnable teamB) {
        int matchTime = 0;
        int teamAScore = 0, teamBScore = 0;

        ((TournamentParticipant) teamA).clearInjuryCount();
        ((TournamentParticipant) teamB).clearInjuryCount();

        teamA.setWinningRate(teamA.getBaseWinningRate());
        teamB.setWinningRate(teamB.getBaseWinningRate());

        while (matchTime < FootballConstant.FULL_TIME.getValue()) {
            // 수비팀 선택
            Winnable defender = determineDefender(teamA, teamB);

            // 수비 실패
            if (!tryDefense(defender)) {
                teamAScore += (defender == teamB) ? 1 : 0;
                teamBScore += (defender == teamA) ? 1 : 0;
            }

            // 부상 로직 적용 -> 공격-수비 팀 선택에 영향을 끼친다.
            applyInjuries(teamA, teamB);

            // 경기 시간 증가
            matchTime += FootballConstant.TIME_SPLIT.getValue();
        }

        if (teamAScore == teamBScore) {
            System.out.println(teamAScore + " : " + teamBScore);
            System.out.println("승부차기 진행");

            return penaltyShootout() ? (TournamentParticipant) teamA : (TournamentParticipant) teamB;
        }

        System.out.println(teamAScore + " : " + teamBScore);
        return teamAScore > teamBScore ? (TournamentParticipant) teamA : (TournamentParticipant) teamB;
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

//    public TournamentParticipant fight(Winnable teamA, Winnable teamB) {
//        applyInjuries(teamA, teamB);
//        return decideWinner(teamA, teamB);
//    }

    private void applyInjuries(Winnable teamA, Winnable teamB) {
        Stream.of(teamA, teamB)
                .filter(Injurable.class::isInstance)
                .map(Injurable.class::cast)
                .forEach(this::injuryOccur);
    }

//    private TournamentParticipant decideWinner(Winnable teamA, Winnable teamB) {
//        double teamAWeight = Math.sqrt(
//                teamA.getWinningRate()
//        );
//
//        double teamBWeight = Math.sqrt(
//                teamB.getWinningRate()
//        );
//
//        double r = rd.nextDouble() * (teamAWeight + teamBWeight);
//
//        if (teamAWeight >= r) return (TournamentParticipant) teamA;
//        return (TournamentParticipant) teamB;
//    }

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