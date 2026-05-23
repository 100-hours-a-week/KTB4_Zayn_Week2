package org.example.common;

public enum ViewMessage {
    INIT_MESSAGE("""
            ------UEFA Champions League Simulator------
            25-26 챔피언스리그 16강 진출 팀들의 대진을 완성하고, 시뮬레이션합니다.
            총 2번(16강, 8강) 대진표를 작성하며 각 경기의 결과는 엔터 키 입력으로 확인합니다.
            """),
    PRESS_ANY_KEY("계속하려면 Enter를 누르세요."),
    MIDDLE_BAR("--------------------------------"),
    ROUND_OF_16_INIT("""
            16강 대진표를 추첨해주세요
            각 팀에 해당하는 숫자를 입력해주세요, 입력된 순서에 따라 대진표가 결정됩니다
            """),
    ROUND_OF_8_INIT("""
            8강 대진표를 추첨해주세요.
            8강 대진에 따라 4강, 결승전의 매치가 결정됩니다.
            각 팀에 해당하는 숫자를 입력해주세요, 입력된 순서에 따라 대진표가 결정됩니다
            """),
    TEAM_NAME_FORMAT("%d: %s "),
    USER_INPUT_FORMAT("입력: "),
    WINNER_MESSAGE("승리팀 정보: %s(%s)"),
    FINAL_WINNER("""
            
            ##############################
            UEFA 25-26 Champions League Final
            최종 우승 팀: %s(%s)
            ##############################
            """),
    MATCH_INFO("""
            %S %d경기
            %s(%s) vs %s(%s)
            """);

    private final String message;

    ViewMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String get(Object... args) {
        return String.format(message, args);
    }
}