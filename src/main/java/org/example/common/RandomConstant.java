package org.example.common;

public enum RandomConstant {
    INJURY_PROBABILITY(0.3),
    DECREASE_RATE(0.8),
    DEFENSE_SCALE(0.3),
    DEFENSE_BASE(0.5);

    private final double value;

    RandomConstant(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
