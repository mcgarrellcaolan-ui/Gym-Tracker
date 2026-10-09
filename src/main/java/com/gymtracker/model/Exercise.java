package com.gymtracker.model;

public class Exercise {
    private final String name;
    private final MuscleGroup muscleGroup;

    public Exercise(String name, MuscleGroup muscleGroup) {
        this.name = name;
        this.muscleGroup = muscleGroup;
    }

    public String getName() {
        return name;
    }

    public MuscleGroup getMuscleGroup() {
        return muscleGroup;
    }

    @Override
    public String toString() {
        return name;
    }
}
