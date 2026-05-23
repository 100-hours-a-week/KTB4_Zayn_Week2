package org.example.controller;

import org.example.common.ErrorMessage;
import org.example.common.TournamentConstant;
import org.example.common.UefaTeamInfo;
import org.example.model.FootballTeam;
import org.example.model.TournamentParticipant;
import org.example.model.Winnable;
import org.example.service.MatchService;
import org.example.view.InputView;
import org.example.view.OutputView;

import java.util.ArrayList;
import java.util.List;

// FootballTeam의 상속으로 UefaTeam클래스를 사용하지않고
// UefaTournamentParticipant 클래스의 필드 객체로 FootballTeam을 가지는 컴포지션 구조로 변경한 컨트롤러
public class UefaController {
    private final InputView iv;
    private final OutputView ov;
    private final MatchService ms;

    private final List<TournamentParticipant> totalTeams = new ArrayList<>();

    public UefaController() {
        this.iv = new InputView();
        this.ov = new OutputView();
        this.ms = new MatchService();
    }

    public void run() {
        init();

        try { // 입력값 검증 필요 과정 -> IllegalArgumentException
            List<TournamentParticipant> winners = playRoundOf16();
            List<TournamentParticipant> semiFinalsTeams = playQuarterFinals(winners);

            playFinal(
                    playSemiFinals(semiFinalsTeams)
            );
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // UEFA 16강 진출 팀 객체 생성 및 안내 메시지 출력
    private void init() {
        for (UefaTeamInfo teamName : UefaTeamInfo.values())
            totalTeams.add(
                    new TournamentParticipant(
                            new FootballTeam(
                                    teamName.getFullName(),
                                    teamName.getShortName(),
                                    teamName.getDefenseAbility()
                            ),
                            teamName.getWinningRate()
                    )
            );

        ov.displayInitMessage();
        pressAnyKey();
    }

    private List<TournamentParticipant> playRoundOf16() {
        ov.displayTeamsMessage(totalTeams);

        // 대진표 입력
        List<TournamentParticipant> matchTeams = createBracket(TournamentConstant.ROUND_OF_16.getValue(), totalTeams);
        // 경기진행 및 결과 조회
        List<TournamentParticipant> winners = progressMatch(TournamentConstant.ROUND_OF_16.getValue(), matchTeams);

        pressAnyKey();
        return winners;
    }

    private List<TournamentParticipant> playQuarterFinals(List<TournamentParticipant> teams) {
        ov.displayTeamsMessage(teams);

        // 대진표 입력
        List<TournamentParticipant> matchTeams = createBracket(TournamentConstant.QUARTER_FINALS.getValue(), teams);
        // 경기 진행 및 결과 조회
        List<TournamentParticipant> winners = progressMatch(TournamentConstant.QUARTER_FINALS.getValue(), matchTeams);

        pressAnyKey();
        return winners;
    }

    private List<TournamentParticipant> playSemiFinals(List<TournamentParticipant> teams) {
        ov.displayTeamsMessage(teams);
        return progressMatch(TournamentConstant.SEMI_FINALS.getValue(), teams);
    }

    private void playFinal(List<TournamentParticipant> teams) {
        ov.printMatchInfo(
                teams.size(),
                TournamentConstant.ONLY_ONE_ROUND.getValue(),
                teams.get(0),
                teams.get(1)
        );
        pressAnyKey();

        ov.finalWinnerMessage(
                ms.threadFight(
                        (Winnable) teams.get(0),
                        (Winnable) teams.get(1)
                )
        );

        iv.close();
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