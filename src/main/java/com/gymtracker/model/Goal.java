package com.gymtracker.model;

/**
 * A target weight for an exercise, e.g. Bench Press 100kg.
 */
public class Goal {
    private final String exercise;
    private final double targetKg;

    public Goal(String exercise, double targetKg) {
        this.exercise = exercise;
        this.targetKg = targetKg;
    }

    public String getExercise() {
        return exercise;
    }

    public double getTargetKg() {
        return targetKg;
    }

    /** Returns progress as a percentage, capped at 100. */
    public double progress(double bestKg) {
        if (targetKg <= 0) {
            return 0;
        }
        return Math.min(100, bestKg / targetKg * 100);
    }
}
