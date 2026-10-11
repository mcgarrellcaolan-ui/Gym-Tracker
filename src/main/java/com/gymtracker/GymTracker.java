package com.gymtracker;

import com.gymtracker.model.BodyWeight;
import com.gymtracker.model.Goal;
import com.gymtracker.model.Lift;
import com.gymtracker.model.Workout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Holds the user's workouts, body weight entries, goals and exercises.
public class GymTracker {
    private final List<Workout> workouts = new ArrayList<>();
    private final List<BodyWeight> bodyWeights = new ArrayList<>();
    private final List<Goal> goals = new ArrayList<>();
    private final ExerciseLibrary exercises = new ExerciseLibrary();

    // Adds a lift to the workout on that date, creating the workout if needed.
    public void addLift(LocalDate date, Lift lift) {
        Workout workout = findWorkout(date);
        if (workout == null) {
            workout = new Workout(date);
            workouts.add(workout);
            workouts.sort(Comparator.comparing(Workout::getDate));
        }
        workout.addLift(lift);
    }

    public Workout findWorkout(LocalDate date) {
        for (Workout workout : workouts) {
            if (workout.getDate().equals(date)) {
                return workout;
            }
        }
        return null;
    }

    public void addBodyWeight(BodyWeight entry) {
        bodyWeights.add(entry);
        bodyWeights.sort(Comparator.comparing(BodyWeight::getDate));
    }

    public void addGoal(Goal goal) {
        goals.add(goal);
    }

    public void removeGoal(int index) {
        goals.remove(index);
    }

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public List<BodyWeight> getBodyWeights() {
        return bodyWeights;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public ExerciseLibrary getExercises() {
        return exercises;
    }
}
