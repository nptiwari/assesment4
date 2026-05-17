package service;

import model.Driver;
import model.Rider;
import util.Display;

import java.util.ArrayList;

/**
 * StatsService - generates the system dashboard with live analytics.
 */
public class StatsService {

    public static void showDashboard(
            ArrayList<Driver> drivers,
            ArrayList<Rider> riders,
            TripService tripService) {

        Display.banner("UBER MANAGEMENT SYSTEM  -  DASHBOARD");
        Display.blank();

        // ── Driver stats ─────────────────────────────────────────────
        Display.subHeader("Driver Overview");

        long available = 0, onTrip = 0, suspended = 0;
        double totalEarn = 0, ratingSum = 0;

        for (Driver d : drivers) {
            if (d.getStatus() == Driver.Status.AVAILABLE)  available++;
            if (d.getStatus() == Driver.Status.ON_TRIP)    onTrip++;
            if (d.getStatus() == Driver.Status.SUSPENDED)  suspended++;
            totalEarn  += d.getTotalEarnings();
            ratingSum  += d.getRating();
        }

        double avgRating = drivers.isEmpty() ? 0 : ratingSum / drivers.size();

        Display.stat("Total drivers",         String.valueOf(drivers.size()));
        Display.stat("Available",             String.valueOf(available));
        Display.stat("On trip",               String.valueOf(onTrip));
        Display.stat("Suspended",             String.valueOf(suspended));
        Display.stat("Avg driver rating",
            String.format("%.1f  %s", avgRating,
                Driver.renderStars(avgRating)));
        Display.stat("Total driver earnings",
            "$" + String.format("%.2f", totalEarn));

        // ── Rider stats ──────────────────────────────────────────────
        Display.subHeader("Rider Overview");

        double totalSpent = 0, riderRatingSum = 0;
        for (Rider r : riders) {
            totalSpent     += r.getTotalSpent();
            riderRatingSum += r.getRiderRating();
        }

        double avgRiderRating = riders.isEmpty() ?
            0 : riderRatingSum / riders.size();

        Display.stat("Total riders",
            String.valueOf(riders.size()));
        Display.stat("Avg rider rating",
            String.format("%.1f  %s", avgRiderRating,
                Driver.renderStars(avgRiderRating)));
        Display.stat("Total rider spending",
            "$" + String.format("%.2f", totalSpent));

        // ── Trip stats ───────────────────────────────────────────────
        Display.subHeader("Trip Overview");
        tripService.showTripStats();

        // ── Top 3 drivers ────────────────────────────────────────────
        Display.subHeader("Top 3 Drivers by Rating");

     // Sort using simple bubble sort (no streams needed)
        ArrayList<Driver> sorted = new ArrayList<>(drivers);
        for (int i = 0; i < sorted.size() - 1; i++) {
            for (int j = 0; j < sorted.size() - 1 - i; j++) {
                if (sorted.get(j).getRating() < sorted.get(j + 1).getRating()) {
                    Driver temp = sorted.get(j);
                    sorted.set(j, sorted.get(j + 1));
                    sorted.set(j + 1, temp);
                }
            }
        }

        int count = Math.min(3, sorted.size());
        for (int i = 0; i < count; i++) {
            Driver d = sorted.get(i);
            System.out.printf("  %-22s  %s %.1f  ($%.2f earned)%n",
                d.getName(),
                Driver.renderStars(d.getRating()),
                d.getRating(),
                d.getTotalEarnings());
        }

        Display.blank();
    }
}