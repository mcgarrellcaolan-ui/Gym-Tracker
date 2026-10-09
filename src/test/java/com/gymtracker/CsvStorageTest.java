package com.gymtracker;

import com.gymtracker.model.BodyWeight;
import com.gymtracker.model.Goal;
import com.gymtracker.model.Lift;
import com.gymtracker.model.MuscleGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CsvStorageTest {

    @TempDir
    Path folder;

    @Test
    void savedDataLoadsBackTheSame() throws IOException {
        GymTracker tracker = new GymTracker();
        LocalDate date = LocalDate.parse("2026-10-01");
        tracker.getExercises().addCustom("Hip Thrust", MuscleGroup.LEGS);
        tracker.addLift(date, new Lift("Squat", 3, 5, 100));
        tracker.addLift(date, new Lift("Hip Thrust", 3, 10, 82.5));
        tracker.addBodyWeight(new BodyWeight(date, 78.4));
        tracker.addGoal(new Goal("Squat", 120));

        CsvStorage storage = new CsvStorage(folder);
        storage.save(tracker);
        GymTracker loaded = storage.load();

        assertEquals(1, loaded.getWorkouts().size());
        assertEquals(2, loaded.getWorkouts().get(0).getLifts().size());
        assertEquals(82.5, loaded.getWorkouts().get(0).getLifts().get(1).getWeightKg());
        assertEquals(78.4, loaded.getBodyWeights().get(0).getWeightKg());
        assertEquals("Squat", loaded.getGoals().get(0).getExercise());
        assertNotNull(loaded.getExercises().find("hip thrust"));
    }

    @Test
    void noFilesMeansNoData() throws IOException {
        CsvStorage storage = new CsvStorage(folder);
        assertFalse(storage.hasData());
        assertEquals(0, storage.load().getWorkouts().size());
    }
}
