package com.gymtracker;

import com.gymtracker.model.Exercise;
import com.gymtracker.model.MuscleGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * The list of exercises to pick from: some built in, plus any the user adds.
 */
public class ExerciseLibrary {
    private final List<Exercise> exercises = new ArrayList<>();
    private final List<Exercise> custom = new ArrayList<>();

    public ExerciseLibrary() {
        addBuiltIn("Bench Press", MuscleGroup.CHEST);
        addBuiltIn("Incline Dumbbell Press", MuscleGroup.CHEST);
        addBuiltIn("Chest Fly", MuscleGroup.CHEST);
        addBuiltIn("Deadlift", MuscleGroup.BACK);
        addBuiltIn("Barbell Row", MuscleGroup.BACK);
        addBuiltIn("Lat Pulldown", MuscleGroup.BACK);
        addBuiltIn("Pull Up", MuscleGroup.BACK);
        addBuiltIn("Squat", MuscleGroup.LEGS);
        addBuiltIn("Leg Press", MuscleGroup.LEGS);
        addBuiltIn("Romanian Deadlift", MuscleGroup.LEGS);
        addBuiltIn("Lunges", MuscleGroup.LEGS);
        addBuiltIn("Overhead Press", MuscleGroup.SHOULDERS);
        addBuiltIn("Lateral Raise", MuscleGroup.SHOULDERS);
        addBuiltIn("Bicep Curl", MuscleGroup.ARMS);
        addBuiltIn("Tricep Pushdown", MuscleGroup.ARMS);
        addBuiltIn("Plank", MuscleGroup.CORE);
    }

    private void addBuiltIn(String name, MuscleGroup group) {
        exercises.add(new Exercise(name, group));
    }

    /** Adds a new exercise. Returns false if one with that name already exists. */
    public boolean addCustom(String name, MuscleGroup group) {
        if (name.isBlank() || find(name) != null) {
            return false;
        }
        Exercise exercise = new Exercise(name.trim(), group);
        exercises.add(exercise);
        custom.add(exercise);
        return true;
    }

    /** Finds an exercise by name, ignoring upper/lower case. Returns null if not found. */
    public Exercise find(String name) {
        for (Exercise e : exercises) {
            if (e.getName().equalsIgnoreCase(name.trim())) {
                return e;
            }
        }
        return null;
    }

    public List<Exercise> getAll() {
        return exercises;
    }

    public List<Exercise> getCustom() {
        return custom;
    }
}
