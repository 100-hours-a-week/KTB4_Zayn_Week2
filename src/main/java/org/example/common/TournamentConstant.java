package org.example.common;

public enum TournamentConstant {
    ROUND_OF_16(16),
    QUARTER_FINALS(8),
    SEMI_FINALS(4),
    FINAL(2),
    ONLY_ONE_ROUND(1),
    SPLIT_NUMBER(4);

    private final int value;

    TournamentConstant(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}