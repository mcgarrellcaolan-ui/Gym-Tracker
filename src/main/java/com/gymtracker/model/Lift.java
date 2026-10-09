package com.gymtracker.model;

// One exercise done in a workout, e.g. Bench Press 3 x 8 at 60kg.
public class Lift {
    private final String exercise;
    private final int sets;
    private final int reps;
    private final double weightKg;

    public Lift(String exercise, int sets, int reps, double weightKg) {
        if (sets < 1 || reps < 1) {
            throw new IllegalArgumentException("Sets and reps must be at least 1");
        }
        if (weightKg < 0) {
            throw new IllegalArgumentException("Weight can't be negative");
        }
        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
        this.weightKg = weightKg;
    }

    public String getExercise() {
        return exercise;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

    public double getWeightKg() {
        return weightKg;
    }

    @Override
    public String toString() {
        return exercise + ": " + sets + " x " + reps + " @ " + weightKg + "kg";
    }
}
