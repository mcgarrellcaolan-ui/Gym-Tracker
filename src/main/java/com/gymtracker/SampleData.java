package com.gymtracker;

import com.gymtracker.model.BodyWeight;
import com.gymtracker.model.Goal;
import com.gymtracker.model.Lift;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Made-up workouts so the app has something to show the first time it runs.
 * Six weeks of push / pull / legs, getting a bit stronger each week.
 */
public class SampleData {

    public static GymTracker create(LocalDate today) {
        GymTracker tracker = new GymTracker();
        LocalDate monday = today.with(DayOfWeek.MONDAY).minusWeeks(5);

        for (int week = 0; week < 6; week++) {
            LocalDate push = monday.plusWeeks(week);
            LocalDate pull = push.plusDays(2);
            LocalDate legs = push.plusDays(4);
            double add = week * 2.5; // +2.5kg a week

            if (!push.isAfter(today)) {
                tracker.addLift(push, new Lift("Bench Press", 3, 8, 60 + add));
                tracker.addLift(push, new Lift("Overhead Press", 3, 8, 35 + add / 2));
                tracker.addLift(push, new Lift("Tricep Pushdown", 3, 12, 25));
            }
            if (!pull.isAfter(today)) {
                tracker.addLift(pull, new Lift("Deadlift", 3, 5, 100 + add * 2));
                tracker.addLift(pull, new Lift("Barbell Row", 3, 8, 50 + add));
                tracker.addLift(pull, new Lift("Bicep Curl", 3, 10, 12.5));
            }
            if (!legs.isAfter(today)) {
                tracker.addLift(legs, new Lift("Squat", 3, 6, 80 + add * 2));
                tracker.addLift(legs, new Lift("Leg Press", 3, 10, 140 + add * 2));
                tracker.addLift(legs, new Lift("Plank", 3, 1, 0));
            }

            if (!push.isAfter(today)) {
                tracker.addBodyWeight(new BodyWeight(push, 78.0 + week * 0.3));
            }
        }

        tracker.addGoal(new Goal("Bench Press", 100));
        tracker.addGoal(new Goal("Squat", 120));
        return tracker;
    }
}
