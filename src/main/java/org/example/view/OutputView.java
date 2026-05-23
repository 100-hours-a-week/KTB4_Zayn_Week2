package org.example.view;

import org.example.common.TournamentConstant;
import org.example.common.RoundName;
import org.example.common.ViewMessage;
import org.example.model.TournamentParticipant;

import java.util.List;

public class OutputView {
    public void displayInitMessage() {
        System.out.println(ViewMessage.INIT_MESSAGE.getMessage());
    }

    public void displayPressAnyKey() {
        System.out.print(ViewMessage.PRESS_ANY_KEY.getMessage());
    }

    public void displayTeamsMessage(List<TournamentParticipant> teams) {
        System.out.println(ViewMessage.MIDDLE_BAR.getMessage());
        if (teams.size() == TournamentConstant.ROUND_OF_16.getValue()) {
            System.out.println(ViewMessage.ROUND_OF_16_INIT.getMessage());
        }

        if (teams.size() == TournamentConstant.QUARTER_FINALS.getValue()) {
            System.out.println(ViewMessage.ROUND_OF_8_INIT.getMessage());
        }

        System.out.println(ViewMessage.MIDDLE_BAR.getMessage());
        for (int i = 0; i < teams.size(); i++) {
            System.out.print(ViewMessage.TEAM_NAME_FORMAT.get(
                    i + 1,
                    teams.get(i).getTeamName()
            ));

            if ((i + 1) % TournamentConstant.SPLIT_NUMBER.getValue() == 0)
                System.out.println();
        }
        System.out.println(ViewMessage.MIDDLE_BAR.getMessage());
    }

    public void printWinner(TournamentParticipant winner) {
        System.out.println(ViewMessage.MIDDLE_BAR.getMessage());
        System.out.println(ViewMessage.WINNER_MESSAGE.get(winner.getTeamName(), winner.getShortName()));
        System.out.println(ViewMessage.MIDDLE_BAR.getMessage());
    }

    public void finalWinnerMessage(TournamentParticipant winner) {
        System.out.println(ViewMessage.FINAL_WINNER.get(winner.getTeamName(), winner.getShortName()));
    }

    public void printMatchInfo(int roundNum, int roundMatchCount, TournamentParticipant teamA, TournamentParticipant teamB) {
        System.out.println(
                ViewMessage.MATCH_INFO.get(
                        RoundName.getRound(roundNum),
                        roundMatchCount,
                        teamA.getTeamName(),
                        teamA.getShortName(),
                        teamB.getTeamName(),
                        teamB.getShortName()
                )
        );
    }
}