package com.gymtracker;

import com.gymtracker.model.Lift;
import com.gymtracker.model.Workout;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatsTest {

    private Workout workout(String date, Lift... lifts) {
        Workout workout = new Workout(LocalDate.parse(date));
        for (Lift lift : lifts) {
            workout.addLift(lift);
        }
        return workout;
    }

    @Test
    void oneRepMaxUsesEpleyFormula() {
        // 100 x (1 + 10/30) = 133.33
        assertEquals(133.33, Stats.oneRepMax(100, 10), 0.01);
    }

    @Test
    void oneRepMaxForSingleRepIsTheWeight() {
        assertEquals(140, Stats.oneRepMax(140, 1));
    }

    @Test
    void personalRecordIsHeaviestWeight() {
        List<Workout> workouts = List.of(
                workout("2026-09-01", new Lift("Bench Press", 3, 8, 60)),
                workout("2026-09-08", new Lift("Bench Press", 3, 5, 70)),
                workout("2026-09-15", new Lift("Bench Press", 3, 8, 65), new Lift("Squat", 3, 5, 100)));

        Map<String, Double> records = Stats.personalRecords(workouts);

        assertEquals(70, records.get("Bench Press"));
        assertEquals(100, records.get("Squat"));
    }

    @Test
    void bodyweightExercisesStillShowInRecords() {
        List<Workout> workouts = List.of(workout("2026-09-01", new Lift("Plank", 3, 1, 0)));
        assertTrue(Stats.personalRecords(workouts).containsKey("Plank"));
    }

    @Test
    void bestOneRepMaxCanComeFromLighterWeight() {
        // 60 x 12 (est. 84) beats 75 x 1 (75)
        List<Workout> workouts = List.of(
                workout("2026-09-01", new Lift("Bench Press", 1, 1, 75)),
                workout("2026-09-08", new Lift("Bench Press", 3, 12, 60)));

        assertEquals(84, Stats.oneRepMaxes(workouts).get("Bench Press"), 0.01);
    }

    @Test
    void streakCountsWeeksInARow() {
        // Thursday 8 Oct 2026. Trained this week and the 2 weeks before.
        LocalDate today = LocalDate.parse("2026-10-08");
        List<Workout> workouts = List.of(
                workout("2026-09-22"),
                workout("2026-09-29"),
                workout("2026-10-06"));

        assertEquals(3, Stats.weekStreak(workouts, today));
    }

    @Test
    void streakNotBrokenIfNotTrainedYetThisWeek() {
        LocalDate today = LocalDate.parse("2026-10-05"); // Monday
        List<Workout> workouts = List.of(workout("2026-09-28"), workout("2026-10-02"));

        assertEquals(1, Stats.weekStreak(workouts, today));
    }

    @Test
    void streakEndsAfterMissedWeek() {
        LocalDate today = LocalDate.parse("2026-10-08");
        List<Workout> workouts = List.of(workout("2026-09-15"), workout("2026-10-07"));

        assertEquals(1, Stats.weekStreak(workouts, today));
    }

    @Test
    void noWorkoutsMeansNoStreak() {
        assertEquals(0, Stats.weekStreak(List.of(), LocalDate.now()));
    }
}
