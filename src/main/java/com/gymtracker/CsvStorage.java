package com.gymtracker;

import com.gymtracker.model.BodyWeight;
import com.gymtracker.model.Exercise;
import com.gymtracker.model.Goal;
import com.gymtracker.model.Lift;
import com.gymtracker.model.MuscleGroup;
import com.gymtracker.model.Workout;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves and loads everything as CSV files, so the data can also be opened in Excel.
 *
 * workouts.csv    date,exercise,sets,reps,weightKg
 * bodyweight.csv  date,weightKg
 * goals.csv       exercise,targetKg
 * exercises.csv   name,muscleGroup   (only the ones the user added)
 */
public class CsvStorage {
    private final Path folder;

    public CsvStorage(Path folder) {
        this.folder = folder;
    }

    public boolean hasData() {
        return Files.exists(folder.resolve("workouts.csv"));
    }

    public void save(GymTracker tracker) throws IOException {
        Files.createDirectories(folder);

        List<String> workoutLines = new ArrayList<>();
        workoutLines.add("date,exercise,sets,reps,weightKg");
        for (Workout workout : tracker.getWorkouts()) {
            for (Lift lift : workout.getLifts()) {
                workoutLines.add(workout.getDate() + "," + lift.getExercise() + ","
                        + lift.getSets() + "," + lift.getReps() + "," + lift.getWeightKg());
            }
        }
        Files.write(folder.resolve("workouts.csv"), workoutLines);

        List<String> weightLines = new ArrayList<>();
        weightLines.add("date,weightKg");
        for (BodyWeight entry : tracker.getBodyWeights()) {
            weightLines.add(entry.getDate() + "," + entry.getWeightKg());
        }
        Files.write(folder.resolve("bodyweight.csv"), weightLines);

        List<String> goalLines = new ArrayList<>();
        goalLines.add("exercise,targetKg");
        for (Goal goal : tracker.getGoals()) {
            goalLines.add(goal.getExercise() + "," + goal.getTargetKg());
        }
        Files.write(folder.resolve("goals.csv"), goalLines);

        List<String> exerciseLines = new ArrayList<>();
        exerciseLines.add("name,muscleGroup");
        for (Exercise exercise : tracker.getExercises().getCustom()) {
            exerciseLines.add(exercise.getName() + "," + exercise.getMuscleGroup());
        }
        Files.write(folder.resolve("exercises.csv"), exerciseLines);
    }

    public GymTracker load() throws IOException {
        GymTracker tracker = new GymTracker();

        // custom exercises first, so lifts that use them can be matched up
        for (String[] row : readRows("exercises.csv")) {
            tracker.getExercises().addCustom(row[0], MuscleGroup.valueOf(row[1]));
        }
        for (String[] row : readRows("workouts.csv")) {
            Lift lift = new Lift(row[1], Integer.parseInt(row[2]),
                    Integer.parseInt(row[3]), Double.parseDouble(row[4]));
            tracker.addLift(LocalDate.parse(row[0]), lift);
        }
        for (String[] row : readRows("bodyweight.csv")) {
            tracker.addBodyWeight(new BodyWeight(LocalDate.parse(row[0]), Double.parseDouble(row[1])));
        }
        for (String[] row : readRows("goals.csv")) {
            tracker.addGoal(new Goal(row[0], Double.parseDouble(row[1])));
        }
        return tracker;
    }

    /** Reads a CSV file, skipping the header and any blank lines. */
    private List<String[]> readRows(String fileName) throws IOException {
        List<String[]> rows = new ArrayList<>();
        Path file = folder.resolve(fileName);
        if (!Files.exists(file)) {
            return rows;
        }
        List<String> lines = Files.readAllLines(file);
        for (int i = 1; i < lines.size(); i++) {
            if (!lines.get(i).isBlank()) {
                rows.add(lines.get(i).split(","));
            }
        }
        return rows;
    }
}
