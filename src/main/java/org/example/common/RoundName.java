package org.example.common;

import java.util.Arrays;

public enum RoundName {
    ROUND_OF_16(16, "16강"),
    QUARTER_FINALS(8, "8강"),
    SEMI_FINALS(4, "4강"),
    FINAL(2, "결승");

    private final int roundNum;
    private final String round;

    RoundName(int roundNum, String round) {
        this.roundNum = roundNum;
        this.round = round;
    }

    public static String getRound(int roundNum) {
        return Arrays.stream(values())
                .filter(r -> r.roundNum == roundNum)
                .map(r -> r.round)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        ErrorMessage.INVALID_PARAMETER.getMessage() + roundNum
                ));
    }
}