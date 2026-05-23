package org.example.common;

public enum RandomConstant {
    INJURY_PROBABILITY(0.3),
    DECREASE_RATE(0.8);

    private final double value;

    RandomConstant(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
