package org.example.common;

public enum FootballConstant {
    FULL_TIME(90),
    TIME_SPLIT(10),
    SHOOTOUT_TIME(1000);

    private final int value;

    FootballConstant(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
