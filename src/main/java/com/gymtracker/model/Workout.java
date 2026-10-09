package com.gymtracker.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Everything done at the gym on one day.
 */
public class Workout {
    private final LocalDate date;
    private final List<Lift> lifts = new ArrayList<>();

    public Workout(LocalDate date) {
        this.date = date;
    }

    public void addLift(Lift lift) {
        lifts.add(lift);
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Lift> getLifts() {
        return lifts;
    }
}
