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

/**
 * Works out personal records, one-rep maxes and the training streak.
 */
public class Stats {

    /**
     * Estimates the most you could lift for one rep using the Epley formula:
     * 1RM = weight x (1 + reps / 30)
     */
    public static double oneRepMax(double weightKg, int reps) {
        if (reps == 1) {
            return weightKg;
        }
        return weightKg * (1 + reps / 30.0);
    }

    /** Heaviest weight lifted for each exercise. */
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

    /** Best estimated one-rep max for each exercise. */
    public static Map<String, Double> bestOneRepMaxes(List<Workout> workouts) {
        Map<String, Double> best = new TreeMap<>();
        for (Workout workout : workouts) {
            for (Lift lift : workout.getLifts()) {
                double estimate = oneRepMax(lift.getWeightKg(), lift.getReps());
                Double current = best.get(lift.getExercise());
                if (current == null || estimate > current) {
                    best.put(lift.getExercise(), estimate);
                }
            }
        }
        return best;
    }

    /**
     * Number of weeks in a row (Monday to Sunday) with at least one workout.
     * The streak isn't broken just because you haven't trained yet this week.
     */
    public static int weekStreak(List<Workout> workouts, LocalDate today) {
        Set<LocalDate> weeksTrained = new HashSet<>();
        for (Workout workout : workouts) {
            weeksTrained.add(startOfWeek(workout.getDate()));
        }

        LocalDate week = startOfWeek(today);
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

    private static LocalDate startOfWeek(LocalDate date) {
        return date.minusDays(date.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
    }
}
