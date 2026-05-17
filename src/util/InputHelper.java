package util;

import java.util.Scanner;

/**
 * InputHelper - safe reading of user input with validation.
 * Prevents crashes from invalid input throughout the system.
 */
public class InputHelper {

    // ── Read a non-empty string ───────────────────────────────────────────
    public static String readString(Scanner sc, String prompt) {
        while (true) {
            System.out.print("  " + prompt + ": ");
            String val = sc.nextLine().trim();
            if (!val.isEmpty()) return val;
            Display.warn("Input cannot be empty. Try again.");
        }
    }

    // ── Read a double within min and max range ────────────────────────────
    public static double readDouble(Scanner sc, String prompt,
            double min, double max) {
        while (true) {
            System.out.print("  " + prompt
                + " (" + min + "-" + max + "): ");
            try {
                double val = Double.parseDouble(
                    sc.nextLine().trim());
                if (val >= min && val <= max) return val;
                Display.warn("Value must be between "
                    + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                Display.warn("Please enter a valid number.");
            }
        }
    }

    // ── Read a positive double ────────────────────────────────────────────
    public static double readPositiveDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print("  " + prompt + ": ");
            try {
                double val = Double.parseDouble(
                    sc.nextLine().trim());
                if (val > 0) return val;
                Display.warn("Value must be greater than 0.");
            } catch (NumberFormatException e) {
                Display.warn("Please enter a valid number.");
            }
        }
    }

    // ── Read an integer within min and max range ──────────────────────────
    public static int readInt(Scanner sc, String prompt,
            int min, int max) {
        while (true) {
            System.out.print("  " + prompt
                + " (" + min + "-" + max + "): ");
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                if (val >= min && val <= max) return val;
                Display.warn("Value must be between "
                    + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                Display.warn("Please enter a whole number.");
            }
        }
    }

    // ── Confirm yes or no ─────────────────────────────────────────────────
    public static boolean confirm(Scanner sc, String prompt) {
        System.out.print("  " + prompt + " (yes/no): ");
        return sc.nextLine().trim().equalsIgnoreCase("yes");
    }

    // ── Read a menu choice ────────────────────────────────────────────────
    public static String readChoice(Scanner sc) {
        System.out.print("\n  Enter choice: ");
        return sc.nextLine().trim();
    }
}