package service;

import model.Driver;
import util.Display;
import util.InputHelper;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * DriverService - handles all CRUD operations for Driver objects.
 * Uses ArrayList<Driver> as the primary collection.
 */
public class DriverService {

    private ArrayList<Driver> drivers;

    public DriverService(ArrayList<Driver> drivers) {
        this.drivers = drivers;
    }

    // ── ADD ──────────────────────────────────────────────────────────────────
    public void addDriver(Scanner sc) {
        Display.section("Add New Driver");
        String id = InputHelper.readString(sc, "Driver ID (e.g. D011)");
        if (findById(id) != null) {
            Display.error("Driver ID " + id + " already exists.");
            return;
        }

        String name    = InputHelper.readString(sc, "Full Name");
        String email   = InputHelper.readString(sc, "Email Address");
        String phone   = InputHelper.readString(sc, "Phone Number");
        String license = InputHelper.readString(sc, "License Number");
        String vtype   = InputHelper.readString(sc, "Vehicle Type (Car/SUV/Van)");
        String vmake   = InputHelper.readString(sc, "Vehicle Make (e.g. Toyota)");
        String vmodel  = InputHelper.readString(sc, "Vehicle Model (e.g. Camry)");
        String vplate  = InputHelper.readString(sc, "Vehicle Plate (e.g. ABC123)");
        int    vyear   = InputHelper.readInt(sc, "Vehicle Year", 2000, 2025);

        Driver d = new Driver(id, name, email, phone, license,
            5.0, 0.0, 0, vtype, vmake, vmodel, vplate, vyear, "AVAILABLE");
        drivers.add(d);
        Display.success("Driver registered: " + name + " [" + id + "]");
    }

    // ── VIEW ALL ─────────────────────────────────────────────────────────────
    public void viewAll() {
        Display.section("All Drivers  (" + drivers.size() + " registered)");
        if (drivers.isEmpty()) {
            Display.info("No drivers registered yet.");
            return;
        }
        for (Driver d : drivers) {
            Display.card(d.getDetails());
        }
    }

    // ── QUERY ────────────────────────────────────────────────────────────────
    public void query(Scanner sc) {
        Display.section("Search Driver");
        Display.menuItem("1", "Search by ID");
        Display.menuItem("2", "Search by Name");
        Display.menuItem("3", "Filter by Status");
        String choice = InputHelper.readChoice(sc);

        switch (choice) {
            case "1":
                String id = InputHelper.readString(sc, "Driver ID");
                Driver d = findById(id);
                if (d != null) Display.card(d.getDetails());
                else Display.error("No driver found with ID: " + id);
                break;

            case "2":
                String name = InputHelper.readString(sc,
                    "Name (partial OK)").toLowerCase();
                boolean found = false;
                for (Driver dr : drivers) {
                    if (dr.getName().toLowerCase().contains(name)) {
                        Display.card(dr.getDetails());
                        found = true;
                    }
                }
                if (!found) Display.error("No drivers matching: " + name);
                break;

            case "3":
                Display.info(
                    "Status options: AVAILABLE / ON_TRIP / SUSPENDED / OFFLINE");
                String st = InputHelper.readString(sc, "Status").toUpperCase();
                boolean any = false;
                for (Driver dr : drivers) {
                    if (dr.getStatus().name().equals(st)) {
                        Display.card(dr.getDetails());
                        any = true;
                    }
                }
                if (!any) Display.info("No drivers with status: " + st);
                break;

            default:
                Display.warn("Invalid option.");
        }
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────
    public void update(Scanner sc) {
        Display.section("Update Driver");
        String id = InputHelper.readString(sc, "Driver ID to update");
        Driver d = findById(id);
        if (d == null) {
            Display.error("Driver not found: " + id);
            return;
        }

        Display.card(d.getDetails());
        Display.blank();
        Display.menuItem("1", "Phone Number");
        Display.menuItem("2", "Email Address");
        Display.menuItem("3", "Rating (admin override)");
        Display.menuItem("4", "Status");
        Display.menuItem("5", "Vehicle Plate");
        Display.menuItem("6", "Vehicle Make/Model");
        String choice = InputHelper.readChoice(sc);

        switch (choice) {
            case "1":
                d.setPhone(InputHelper.readString(sc, "New Phone"));
                Display.success("Phone updated.");
                break;
            case "2":
                d.setEmail(InputHelper.readString(sc, "New Email"));
                Display.success("Email updated.");
                break;
            case "3":
                double r = InputHelper.readDouble(sc, "New Rating", 1.0, 5.0);
                d.setRating(r);
                Display.success("Rating updated to " + r);
                break;
            case "4":
                Display.info(
                    "Options: AVAILABLE / ON_TRIP / SUSPENDED / OFFLINE");
                String st = InputHelper.readString(sc, "New Status").toUpperCase();
                try {
                    d.setStatus(Driver.Status.valueOf(st));
                    Display.success("Status updated to " + st);
                } catch (Exception e) {
                    Display.error("Invalid status: " + st);
                }
                break;
            case "5":
                d.setVehiclePlate(InputHelper.readString(sc, "New Plate"));
                Display.success("Plate updated.");
                break;
            case "6":
                d.setVehicleMake(InputHelper.readString(sc, "New Make"));
                d.setVehicleModel(InputHelper.readString(sc, "New Model"));
                Display.success("Vehicle updated.");
                break;
            default:
                Display.warn("Invalid option.");
        }
    }

    // ── DELETE ───────────────────────────────────────────────────────────────
    public void delete(Scanner sc) {
        Display.section("Delete Driver");
        String id = InputHelper.readString(sc, "Driver ID to delete");
        Driver d = findById(id);
        if (d == null) {
            Display.error("Driver not found: " + id);
            return;
        }

        Display.card(d.getDetails());
        if (InputHelper.confirm(sc, "Permanently delete " + d.getName() + "?")) {
            drivers.remove(d);
            Display.success("Driver deleted: " + d.getName());
        } else {
            Display.info("Delete cancelled.");
        }
    }

    // ── EVALUATIONS ──────────────────────────────────────────────────────────
    public void runEvaluations() {
        Display.section("Driver Performance Evaluations");
        if (drivers.isEmpty()) {
            Display.info("No drivers to evaluate.");
            return;
        }

        int bonusCount = 0, warnCount = 0, susCount = 0;
        for (Driver d : drivers) {
            System.out.printf("%n  %-20s  Rating: %s %.1f%n",
                d.getName(),
                Driver.renderStars(d.getRating()),
                d.getRating());
            String result = d.evaluatePerformance();
            System.out.println(result);
            if (result.contains("BONUS"))     bonusCount++;
            if (result.contains("WARNING"))   warnCount++;
            if (result.contains("SUSPENDED")) susCount++;
        }
        Display.divider();
        Display.stat("Bonuses awarded",   String.valueOf(bonusCount));
        Display.stat("Warnings issued",   String.valueOf(warnCount));
        Display.stat("Drivers suspended", String.valueOf(susCount));
    }

    // ── HELPERS ──────────────────────────────────────────────────────────────
    public Driver findById(String id) {
        for (Driver d : drivers) {
            if (d.getId().equalsIgnoreCase(id)) return d;
        }
        return null;
    }

    public ArrayList<Driver> getDrivers() { return drivers; }
}