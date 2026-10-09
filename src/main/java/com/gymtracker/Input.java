package com.gymtracker;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

// Reads input from the keyboard and keeps asking until the answer is valid.
public class Input {
    private final Scanner scanner;

    public Input(Scanner scanner) {
        this.scanner = scanner;
    }

    public String text(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int number(String prompt, int min, int max) {
        while (true) {
            String answer = text(prompt);
            try {
                int value = Integer.parseInt(answer);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // fall through to the message below
            }
            System.out.println("Please enter a number from " + min + " to " + max + ".");
        }
    }

    public double decimal(String prompt) {
        while (true) {
            String answer = text(prompt);
            try {
                double value = Double.parseDouble(answer);
                if (value >= 0) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // fall through to the message below
            }
            System.out.println("Please enter a number, e.g. 62.5");
        }
    }

    // Asks for a date. Pressing Enter means today.
    public LocalDate date(String prompt) {
        while (true) {
            String answer = text(prompt);
            if (answer.isEmpty()) {
                return LocalDate.now();
            }
            try {
                return LocalDate.parse(answer);
            } catch (DateTimeParseException e) {
                System.out.println("Please use the format YYYY-MM-DD, e.g. 2026-10-09");
            }
        }
    }

    public boolean yesNo(String prompt) {
        return text(prompt + " (y/n): ").toLowerCase().startsWith("y");
    }
}
