# Gym Tracker

A command-line gym tracker written in Java. You can log workouts and body weight, see
your personal records and estimated one-rep max, keep a weekly training streak and set
goals. Everything is saved to CSV files, so you can also open your data in Excel.

## Features

- Log workouts: exercise, sets, reps and weight (kg)
- Built-in exercise list grouped by muscle, and you can add your own
- Workout history, newest first, with a filter by exercise
- Personal records and estimated one-rep max for every exercise
- Weekly training streak
- Body weight log showing your change over time
- Goals, e.g. "Bench Press 100kg", with a progress bar
- Sample workouts the first time you run it

## Example

```
=== Gym Tracker ===

1. Log a workout
2. Workout history
3. Stats (PRs, one-rep max, streak)
4. Log body weight
5. Body weight history
6. Goals
7. Exercises
0. Exit
Choose an option: 3

Workouts logged: 18
Training streak: 6 week(s) in a row

Exercise                       Best       Est. 1RM
Bench Press                  72.5kg         91.8kg
Deadlift                    125.0kg        145.8kg
Squat                       105.0kg        126.0kg
```

## How to run

You need Java 21 or newer.

```
mvnw package
java -jar target/gym-tracker.jar
```

On Windows use `mvnw.cmd package`. To run the tests: `mvnw test`.

## Project structure

```
src/main/java/com/gymtracker/
    Main.java             menu and user input/output
    Input.java            reads and checks keyboard input
    GymTracker.java       holds all the data (workouts, body weight, goals)
    ExerciseLibrary.java  built-in exercises + ones the user adds
    Stats.java            PRs, one-rep max and streak calculations
    CsvStorage.java       saves and loads the CSV files
    SampleData.java       fake workouts for the first run
    model/
        Workout.java      one day's training
        Lift.java         one exercise in a workout (sets x reps @ weight)
        Exercise.java     exercise name and muscle group
        MuscleGroup.java  enum: CHEST, BACK, LEGS...
        BodyWeight.java   date and weight
        Goal.java         target weight for an exercise
src/test/java/com/gymtracker/
    StatsTest.java, CsvStorageTest.java, GymTrackerTest.java
```

## How I built it

- [x] Planned the features: logging lifts and body weight, PRs, one-rep max, streak, goals
- [x] Set up a Maven project with JUnit for testing
- [x] Wrote the model classes (`Workout`, `Lift`, `Exercise`, `BodyWeight`, `Goal`) and a `MuscleGroup` enum
- [x] Added checks in `Lift` so sets and reps can't be 0 and weight can't be negative
- [x] Built `ExerciseLibrary` with a starting list and a case-insensitive search so "bench press" and "Bench Press" match
- [x] Wrote `GymTracker` to hold the data in `ArrayList`s and group lifts on the same day into one workout
- [x] Wrote `Stats`:
  - personal records using a `TreeMap`, so exercises come out in alphabetical order
  - one-rep max using the Epley formula: `weight x (1 + reps / 30)`
  - streak by putting each workout's week into a `HashSet` and counting back week by week
- [x] Wrote `CsvStorage` to save and load the data with `Files.write` / `Files.readAllLines`
- [x] Built the menu in `Main` with a `switch`, and an `Input` class that keeps asking until the input is valid
- [x] Added goals with a text progress bar
- [x] Added sample data so the app isn't empty on the first run
- [x] Wrote 16 JUnit tests for the stats, saving/loading and validation
- [x] Ran it by hand to check every menu option

## What I'd add next

- Edit or delete a logged workout
- Workout templates (e.g. Push / Pull / Legs)
- A simple graph of progress for one exercise
