package org.example.controller;

import org.example.common.ErrorMessage;
import org.example.common.RoundName;
import org.example.common.TournamentConstant;
import org.example.common.UefaTeamInfo;
import org.example.log.TournamentLogger;
import org.example.model.FootballTeam;
import org.example.model.TournamentParticipant;
import org.example.service.MatchService;
import org.example.view.InputView;
import org.example.view.OutputView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EnhancedUefaController {
    private final InputView iv;
    private final OutputView ov;
    private final MatchService ms;

    private List<TournamentParticipant> teams = new ArrayList<>();
    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    public EnhancedUefaController() {
        this.iv = new InputView();
        this.ov = new OutputView();
        this.ms = new MatchService();
    }

    public void run() {
        init();

        try {
            bracketAndProgressMatch(TournamentConstant.ROUND_OF_16.getValue());
            bracketAndProgressMatch(TournamentConstant.QUARTER_FINALS.getValue());
            playSemiFinals(TournamentConstant.SEMI_FINALS.getValue());
            playFinal(TournamentConstant.FINAL.getValue());

        } catch (IllegalArgumentException | ExecutionException | InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            iv.close();
            pool.shutdown();
        }
    }

    private void init() {
        for (UefaTeamInfo teamName : UefaTeamInfo.values()) {
            FootballTeam tempFootballTeam = new FootballTeam(
                    teamName.getFullName(),
                    teamName.getShortName(),
                    teamName.getDefenseAbility()
            );

            teams.add(new TournamentParticipant(tempFootballTeam, teamName.getWinningRate()));
        }

        ov.displayInitMessage();
        pressAnyKey();
    }

    private void bracketAndProgressMatch(int roundInfo) throws ExecutionException, InterruptedException {
        ov.displayTeamsMessage(teams);
        teams = createBracket(teams.size(), teams);
        teams = progressMatch(roundInfo, teams);
    }

    private void playSemiFinals(int roundInfo) throws ExecutionException, InterruptedException {
        ov.displayTeamsMessage(teams);
        teams = progressMatch(roundInfo, teams);
    }

    private void playFinal(int roundInfo) throws ExecutionException, InterruptedException {
        teams = progressMatch(roundInfo, teams);
        ov.finalWinnerMessage(teams.getFirst());
    }

    private List<TournamentParticipant> createBracket(int teamsCount, List<TournamentParticipant> teams) {
        List<TournamentParticipant> matchTeams = new ArrayList<>();

        boolean[] isSelected = new boolean[teamsCount];
        for (int i = 0; i < teamsCount; i++) {
            int footballTeamIdx = iv.userInput();
            validateIdxScope(teamsCount, footballTeamIdx, isSelected);

            matchTeams.add(teams.get(footballTeamIdx));
        }

        return matchTeams;
    }

    private List<TournamentParticipant> progressMatch(int teamsCount, List<TournamentParticipant> teams)
            throws InterruptedException, ExecutionException {
        List<TournamentParticipant> winners = new ArrayList<>();
        List<TournamentParticipant> losers = new ArrayList<>();
        List<Future<?>> futures = new ArrayList<>();

        long start = System.currentTimeMillis();

// 단일스레드 실행 로직
        for (int i = 0; i < teamsCount; i += 2) {
            TournamentParticipant winner = ms.fight(
                    teams.get(i),
                    teams.get(i + 1)
            );

            TournamentParticipant loser = (winner == teams.get(i)) ? teams.get(i + 1) : teams.get(i);
            winners.add(winner);
            losers.add(loser);
        }

// 멀티스레드 적용 로직
//        for (int i = 0; i < teamsCount; i += 2) {
//            final int idx = i;
//            futures.add(pool.submit(() -> {
//                TournamentParticipant winner = ms.fight(
//                        teams.get(idx),
//                        teams.get(idx + 1)
//                );
//                TournamentParticipant loser = (winner == teams.get(idx)) ? teams.get(idx + 1) : teams.get(idx);
//
//                synchronized (winners) {
//                    winners.add(winner);
//                }
//                synchronized (losers) {
//                    losers.add(loser);
//                }
//            }));
//        }
//
//        for (Future<?> future : futures)
//            future.get();

        long end = System.currentTimeMillis();

        TournamentLogger.saveLog(RoundName.getRound(teamsCount), (end - start));

        int roundMatchCount = 0;
        for (int i = 0; i < teamsCount; i += 2) {
            ov.printMatchInfo(teamsCount, roundMatchCount + 1, teams.get(i), teams.get(i + 1));
            pressAnyKey();
            ov.printMatchResult(winners.get(roundMatchCount), losers.get(roundMatchCount++));
        }

        return winners;
    }

    private void validateIdxScope(int round, int idx, boolean[] isSelected) {
        if (idx < 0 || idx > (round - 1))
            throw new IllegalArgumentException(ErrorMessage.INVALID_NUMBER_SCOPE.getMessage());
        if (isSelected[idx])
            throw new IllegalArgumentException(ErrorMessage.ALREADY_SELECTED_TEAM.getMessage());

        isSelected[idx] = true;
    }

    private void pressAnyKey() {
        ov.displayPressAnyKey();
        iv.pressAnyKey();
    }
}