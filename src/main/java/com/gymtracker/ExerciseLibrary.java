package com.gymtracker;

import com.gymtracker.model.Exercise;
import com.gymtracker.model.MuscleGroup;

import java.util.ArrayList;
import java.util.List;

public class ExerciseLibrary {
    private final List<Exercise> exercises = new ArrayList<>();
    private final List<Exercise> custom = new ArrayList<>(); // ones the user added, saved to exercises.csv

    public ExerciseLibrary() {
        exercises.add(new Exercise("Bench Press", MuscleGroup.CHEST));
        exercises.add(new Exercise("Incline Dumbbell Press", MuscleGroup.CHEST));
        exercises.add(new Exercise("Chest Fly", MuscleGroup.CHEST));
        exercises.add(new Exercise("Deadlift", MuscleGroup.BACK));
        exercises.add(new Exercise("Barbell Row", MuscleGroup.BACK));
        exercises.add(new Exercise("Lat Pulldown", MuscleGroup.BACK));
        exercises.add(new Exercise("Pull Up", MuscleGroup.BACK));
        exercises.add(new Exercise("Squat", MuscleGroup.LEGS));
        exercises.add(new Exercise("Leg Press", MuscleGroup.LEGS));
        exercises.add(new Exercise("Romanian Deadlift", MuscleGroup.LEGS));
        exercises.add(new Exercise("Lunges", MuscleGroup.LEGS));
        exercises.add(new Exercise("Overhead Press", MuscleGroup.SHOULDERS));
        exercises.add(new Exercise("Lateral Raise", MuscleGroup.SHOULDERS));
        exercises.add(new Exercise("Bicep Curl", MuscleGroup.ARMS));
        exercises.add(new Exercise("Tricep Pushdown", MuscleGroup.ARMS));
        exercises.add(new Exercise("Plank", MuscleGroup.CORE));
    }

    // Returns false if the name is blank or the exercise already exists.
    public boolean addCustom(String name, MuscleGroup group) {
        if (name.isBlank() || find(name) != null) {
            return false;
        }
        Exercise exercise = new Exercise(name.trim(), group);
        exercises.add(exercise);
        custom.add(exercise);
        return true;
    }

    // Case-insensitive, so "bench press" finds "Bench Press". Returns null if not found.
    public Exercise find(String name) {
        for (Exercise exercise : exercises) {
            if (exercise.getName().equalsIgnoreCase(name.trim())) {
                return exercise;
            }
        }
        return null;
    }

    public List<Exercise> getByGroup(MuscleGroup group) {
        List<Exercise> result = new ArrayList<>();
        for (Exercise exercise : exercises) {
            if (exercise.getMuscleGroup() == group) {
                result.add(exercise);
            }
        }
        return result;
    }

    public List<Exercise> getCustom() {
        return custom;
    }
}
