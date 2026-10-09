package com.gymtracker;

import com.gymtracker.model.Lift;
import com.gymtracker.model.Workout;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Stats {

    // Epley formula: estimated one-rep max = weight x (1 + reps / 30)
    public static double oneRepMax(double weight, int reps) {
        if (reps == 1) {
            return weight;
        }
        return weight * (1 + reps / 30.0);
    }

    // Heaviest weight lifted for each exercise.
    // TreeMap keeps the exercises in alphabetical order.
    public static Map<String, Double> personalRecords(List<Workout> workouts) {
        Map<String, Double> records = new TreeMap<>();
        for (Workout workout : workouts) {
            for (Lift lift : workout.getLifts()) {
                Double best = records.get(lift.getExercise());
                if (best == null || lift.getWeightKg() > best) {
                    records.put(lift.getExercise(), lift.getWeightKg());
                }
            }
        }
        return records;
    }

    // Best estimated one-rep max for each exercise.
    public static Map<String, Double> oneRepMaxes(List<Workout> workouts) {
        Map<String, Double> maxes = new TreeMap<>();
        for (Workout workout : workouts) {
            for (Lift lift : workout.getLifts()) {
                double estimate = oneRepMax(lift.getWeightKg(), lift.getReps());
                Double best = maxes.get(lift.getExercise());
                if (best == null || estimate > best) {
                    maxes.put(lift.getExercise(), estimate);
                }
            }
        }
        return maxes;
    }

    // How many weeks in a row had at least one workout.
    // If you haven't trained yet this week, the streak counts from last week.
    public static int weekStreak(List<Workout> workouts, LocalDate today) {
        Set<LocalDate> weeksTrained = new HashSet<>();
        for (Workout workout : workouts) {
            weeksTrained.add(mondayOf(workout.getDate()));
        }

        LocalDate week = mondayOf(today);
        if (!weeksTrained.contains(week)) {
            week = week.minusWeeks(1);
        }

        int streak = 0;
        while (weeksTrained.contains(week)) {
            streak++;
            week = week.minusWeeks(1);
        }
        return streak;
    }

    private static LocalDate mondayOf(LocalDate date) {
        return date.with(DayOfWeek.MONDAY);
    }
}
