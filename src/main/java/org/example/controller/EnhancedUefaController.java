package org.example.controller;

import org.example.common.ErrorMessage;
import org.example.common.TournamentConstant;
import org.example.common.UefaTeamInfo;
import org.example.model.FootballTeam;
import org.example.model.TournamentParticipant;
import org.example.service.MatchService;
import org.example.view.InputView;
import org.example.view.OutputView;

import java.util.ArrayList;
import java.util.List;

public class EnhancedUefaController {
    private final InputView iv;
    private final OutputView ov;
    private final MatchService ms;

    private List<TournamentParticipant> teams = new ArrayList<>();

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

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } finally {
            iv.close();
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

    private void bracketAndProgressMatch(int roundInfo) {
        ov.displayTeamsMessage(teams);
        teams = createBracket(teams.size(), teams); // 대진 작성
        teams = progressMatch(roundInfo, teams); // 경기 진행
    }

    private void playSemiFinals(int roundInfo) {
        ov.displayTeamsMessage(teams);
        teams = progressMatch(roundInfo, teams);
    }

    private void playFinal(int roundInfo) {
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

    private List<TournamentParticipant> progressMatch(int teamsCount, List<TournamentParticipant> teams) {
        List<TournamentParticipant> winners = new ArrayList<>();

        int roundMatchCount = 0;
        for (int i = 0; i < teamsCount; i += 2) {
            ov.printMatchInfo(teamsCount, ++roundMatchCount, teams.get(i), teams.get(i + 1));
            pressAnyKey();

            TournamentParticipant winner = ms.threadFight(
                    teams.get(i),
                    teams.get(i + 1)
            );
            winners.add(winner);
            ov.printWinner(winner);
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