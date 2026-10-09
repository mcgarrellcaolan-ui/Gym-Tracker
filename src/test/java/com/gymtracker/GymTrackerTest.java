package com.gymtracker;

import com.gymtracker.model.Goal;
import com.gymtracker.model.Lift;
import com.gymtracker.model.MuscleGroup;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GymTrackerTest {

    @Test
    void liftsOnSameDayGoInOneWorkout() {
        GymTracker tracker = new GymTracker();
        LocalDate today = LocalDate.parse("2026-10-09");
        tracker.addLift(today, new Lift("Squat", 3, 5, 100));
        tracker.addLift(today, new Lift("Leg Press", 3, 10, 150));

        assertEquals(1, tracker.getWorkouts().size());
        assertEquals(2, tracker.getWorkouts().get(0).getLifts().size());
    }

    @Test
    void workoutsAreKeptInDateOrder() {
        GymTracker tracker = new GymTracker();
        tracker.addLift(LocalDate.parse("2026-10-09"), new Lift("Squat", 3, 5, 100));
        tracker.addLift(LocalDate.parse("2026-10-01"), new Lift("Squat", 3, 5, 95));

        assertEquals(LocalDate.parse("2026-10-01"), tracker.getWorkouts().get(0).getDate());
    }

    @Test
    void cantAddTheSameExerciseTwice() {
        ExerciseLibrary library = new ExerciseLibrary();
        assertTrue(library.addCustom("Hip Thrust", MuscleGroup.LEGS));
        assertFalse(library.addCustom("hip thrust", MuscleGroup.LEGS));
        assertFalse(library.addCustom("Bench Press", MuscleGroup.CHEST));
    }

    @Test
    void liftNeedsAtLeastOneRep() {
        assertThrows(IllegalArgumentException.class, () -> new Lift("Squat", 3, 0, 100));
    }

    @Test
    void goalProgressIsCappedAt100() {
        Goal goal = new Goal("Bench Press", 100);
        assertEquals(75, goal.progress(75));
        assertEquals(100, goal.progress(110));
    }
}
