package com.gymtracker;

import com.gymtracker.model.BodyWeight;
import com.gymtracker.model.Exercise;
import com.gymtracker.model.Goal;
import com.gymtracker.model.Lift;
import com.gymtracker.model.MuscleGroup;
import com.gymtracker.model.Workout;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private final GymTracker tracker;
    private final CsvStorage storage;
    private final Input input;

    public Main(GymTracker tracker, CsvStorage storage, Input input) {
        this.tracker = tracker;
        this.storage = storage;
        this.input = input;
    }

    public static void main(String[] args) throws IOException {
        CsvStorage storage = new CsvStorage(Path.of("data"));
        GymTracker tracker;

        if (storage.hasData()) {
            tracker = storage.load();
        } else {
            System.out.println("No saved data found, loading sample workouts.");
            tracker = SampleData.create(LocalDate.now());
            storage.save(tracker);
        }

        new Main(tracker, storage, new Input(new Scanner(System.in))).run();
    }

    public void run() {
        System.out.println("=== Gym Tracker ===");
        try {
            while (true) {
                System.out.println();
                System.out.println("1. Log a workout");
                System.out.println("2. Workout history");
                System.out.println("3. Stats (PRs, one-rep max, streak)");
                System.out.println("4. Log body weight");
                System.out.println("5. Body weight history");
                System.out.println("6. Goals");
                System.out.println("7. Exercises");
                System.out.println("0. Exit");

                int choice = input.number("Choose an option: ", 0, 7);
                switch (choice) {
                    case 1 -> logWorkout();
                    case 2 -> showHistory();
                    case 3 -> showStats();
                    case 4 -> logBodyWeight();
                    case 5 -> showBodyWeight();
                    case 6 -> goalsMenu();
                    case 7 -> exercisesMenu();
                    case 0 -> {
                        System.out.println("See you next session!");
                        return;
                    }
                }
            }
        } catch (NoSuchElementException e) {
            // input was closed (e.g. Ctrl+Z), so just exit
        }
    }

    private void save() {
        try {
            storage.save(tracker);
        } catch (IOException e) {
            System.out.println("Couldn't save: " + e.getMessage());
        }
    }

    private static String kg(double weight) {
        return String.format("%.1fkg", weight);
    }

    // ---- 1. Log a workout ----

    private void logWorkout() {
        LocalDate date = input.date("Date (YYYY-MM-DD, or Enter for today): ");
        do {
            Exercise exercise = chooseExercise();
            int sets = input.number("Sets: ", 1, 20);
            int reps = input.number("Reps: ", 1, 100);
            double weight = input.decimal("Weight (kg, 0 for bodyweight): ");

            Lift lift = new Lift(exercise.getName(), sets, reps, weight);
            tracker.addLift(date, lift);
            System.out.println("Added " + lift);

            Double record = Stats.personalRecords(tracker.getWorkouts()).get(exercise.getName());
            if (weight > 0 && record != null && weight == record) {
                System.out.println("That's your heaviest " + exercise.getName() + " yet!");
            }
        } while (input.yesNo("Add another exercise?"));
        save();
    }

    /** Shows the exercises grouped by muscle and lets the user pick one by number. */
    private Exercise chooseExercise() {
        List<Exercise> numbered = new ArrayList<>();
        for (MuscleGroup group : MuscleGroup.values()) {
            boolean printedHeading = false;
            for (Exercise exercise : tracker.getExercises().getAll()) {
                if (exercise.getMuscleGroup() == group) {
                    if (!printedHeading) {
                        System.out.println(group + ":");
                        printedHeading = true;
                    }
                    numbered.add(exercise);
                    System.out.println("  " + numbered.size() + ". " + exercise.getName());
                }
            }
        }
        System.out.println("  0. Add a new exercise");

        int choice = input.number("Exercise: ", 0, numbered.size());
        if (choice == 0) {
            return addExercise();
        }
        return numbered.get(choice - 1);
    }

    // ---- 2. History ----

    private void showHistory() {
        List<Workout> workouts = tracker.getWorkouts();
        if (workouts.isEmpty()) {
            System.out.println("No workouts yet.");
            return;
        }
        String filter = input.text("Filter by exercise (or Enter for all): ");

        // newest first, last 10 workouts
        int shown = 0;
        for (int i = workouts.size() - 1; i >= 0 && shown < 10; i--) {
            Workout workout = workouts.get(i);
            List<Lift> lifts = new ArrayList<>();
            for (Lift lift : workout.getLifts()) {
                if (filter.isEmpty() || lift.getExercise().equalsIgnoreCase(filter)) {
                    lifts.add(lift);
                }
            }
            if (!lifts.isEmpty()) {
                System.out.println(workout.getDate() + " (" + workout.getDate().getDayOfWeek() + ")");
                for (Lift lift : lifts) {
                    System.out.println("  " + lift);
                }
                shown++;
            }
        }
        if (shown == 0) {
            System.out.println("No workouts found for " + filter + ".");
        }
    }

    // ---- 3. Stats ----

    private void showStats() {
        List<Workout> workouts = tracker.getWorkouts();
        Map<String, Double> records = Stats.personalRecords(workouts);
        Map<String, Double> oneRepMaxes = Stats.bestOneRepMaxes(workouts);

        System.out.println("Workouts logged: " + workouts.size());
        System.out.println("Training streak: " + Stats.weekStreak(workouts, LocalDate.now()) + " week(s) in a row");
        System.out.println();
        System.out.printf("%-24s %10s %14s%n", "Exercise", "Best", "Est. 1RM");
        for (String exercise : records.keySet()) {
            System.out.printf("%-24s %10s %14s%n", exercise, kg(records.get(exercise)), kg(oneRepMaxes.get(exercise)));
        }
    }

    // ---- 4 & 5. Body weight ----

    private void logBodyWeight() {
        LocalDate date = input.date("Date (YYYY-MM-DD, or Enter for today): ");
        double weight = input.decimal("Body weight (kg): ");
        tracker.addBodyWeight(new BodyWeight(date, weight));
        save();
        System.out.println("Saved.");
    }

    private void showBodyWeight() {
        List<BodyWeight> entries = tracker.getBodyWeights();
        if (entries.isEmpty()) {
            System.out.println("No body weight entries yet.");
            return;
        }
        for (BodyWeight entry : entries) {
            System.out.println(entry.getDate() + "  " + kg(entry.getWeightKg()));
        }
        double change = entries.get(entries.size() - 1).getWeightKg() - entries.get(0).getWeightKg();
        System.out.printf("Change since %s: %+.1fkg%n", entries.get(0).getDate(), change);
    }

    // ---- 6. Goals ----

    private void goalsMenu() {
        List<Goal> goals = tracker.getGoals();
        Map<String, Double> records = Stats.personalRecords(tracker.getWorkouts());

        if (goals.isEmpty()) {
            System.out.println("No goals yet.");
        }
        for (int i = 0; i < goals.size(); i++) {
            Goal goal = goals.get(i);
            double best = records.getOrDefault(goal.getExercise(), 0.0);
            double percent = goal.progress(best);
            System.out.printf("%d. %s %s  %s %.0f%% (best %s)%n", i + 1, goal.getExercise(),
                    kg(goal.getTargetKg()), progressBar(percent), percent, kg(best));
        }

        System.out.println();
        System.out.println("1. Add a goal   2. Remove a goal   0. Back");
        int choice = input.number("Choose an option: ", 0, 2);
        if (choice == 1) {
            Exercise exercise = chooseExercise();
            double target = input.decimal("Target weight (kg): ");
            tracker.addGoal(new Goal(exercise.getName(), target));
            save();
        } else if (choice == 2 && !goals.isEmpty()) {
            int number = input.number("Goal number to remove: ", 1, goals.size());
            tracker.removeGoal(number - 1);
            save();
        }
    }

    private static String progressBar(double percent) {
        int filled = (int) (percent / 10);
        return "[" + "#".repeat(filled) + "-".repeat(10 - filled) + "]";
    }

    // ---- 7. Exercises ----

    private void exercisesMenu() {
        for (MuscleGroup group : MuscleGroup.values()) {
            List<String> names = new ArrayList<>();
            for (Exercise exercise : tracker.getExercises().getAll()) {
                if (exercise.getMuscleGroup() == group) {
                    names.add(exercise.getName());
                }
            }
            if (!names.isEmpty()) {
                System.out.println(group + ": " + String.join(", ", names));
            }
        }
        if (input.yesNo("Add a new exercise?")) {
            addExercise();
        }
    }

    private Exercise addExercise() {
        while (true) {
            String name = input.text("Exercise name: ");
            if (name.isEmpty() || name.contains(",")) {
                System.out.println("Please enter a name without commas.");
                continue;
            }
            Exercise existing = tracker.getExercises().find(name);
            if (existing != null) {
                System.out.println(existing.getName() + " is already in the list.");
                return existing;
            }

            MuscleGroup[] groups = MuscleGroup.values();
            for (int i = 0; i < groups.length; i++) {
                System.out.println("  " + (i + 1) + ". " + groups[i]);
            }
            MuscleGroup group = groups[input.number("Muscle group: ", 1, groups.length) - 1];

            tracker.getExercises().addCustom(name, group);
            save();
            System.out.println("Added " + name + ".");
            return tracker.getExercises().find(name);
        }
    }
}
