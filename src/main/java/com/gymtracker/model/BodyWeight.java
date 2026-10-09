package com.gymtracker.model;

import java.time.LocalDate;

public class BodyWeight {
    private final LocalDate date;
    private final double weightKg;

    public BodyWeight(LocalDate date, double weightKg) {
        this.date = date;
        this.weightKg = weightKg;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getWeightKg() {
        return weightKg;
    }
}
