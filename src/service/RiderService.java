package service;

import model.Rider;
import util.Display;
import util.InputHelper;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * RiderService - handles all CRUD operations for Rider objects.
 * Uses ArrayList<Rider> as the primary collection.
 */
public class RiderService {

    private ArrayList<Rider> riders;

    public RiderService(ArrayList<Rider> riders) {
        this.riders = riders;
    }

    // ── ADD ──────────────────────────────────────────────────────────────────
    public void addRider(Scanner sc) {
        Display.section("Add New Rider");
        String id = InputHelper.readString(sc, "Rider ID (e.g. R011)");
        if (findById(id) != null) {
            Display.error("Rider ID " + id + " already exists.");
            return;
        }

        String name    = InputHelper.readString(sc, "Full Name");
        String email   = InputHelper.readString(sc, "Email Address");
        String phone   = InputHelper.readString(sc, "Phone Number");
        Display.info(
            "Payment options: CREDIT_CARD / CASH / PAYPAL / APPLE_PAY");
        String payment = InputHelper.readString(sc, "Payment Method");
        String address = InputHelper.readString(sc, "Home Address");

        riders.add(new Rider(id, name, email, phone,
            5.0, payment, address, 0.0));
        Display.success("Rider registered: " + name + " [" + id + "]");
    }

    // ── VIEW ALL ─────────────────────────────────────────────────────────────
    public void viewAll() {
        Display.section("All Riders  (" + riders.size() + " registered)");
        if (riders.isEmpty()) {
            Display.info("No riders registered yet.");
            return;
        }
        for (Rider r : riders) {
            Display.card(r.getDetails());
        }
    }

    // ── QUERY ────────────────────────────────────────────────────────────────
    public void query(Scanner sc) {
        Display.section("Search Rider");
        Display.menuItem("1", "Search by ID");
        Display.menuItem("2", "Search by Name");
        String choice = InputHelper.readChoice(sc);

        if (choice.equals("1")) {
            String id = InputHelper.readString(sc, "Rider ID");
            Rider r = findById(id);
            if (r != null) Display.card(r.getDetails());
            else Display.error("No rider found with ID: " + id);

        } else if (choice.equals("2")) {
            String name = InputHelper.readString(sc,
                "Name (partial OK)").toLowerCase();
            boolean found = false;
            for (Rider r : riders) {
                if (r.getName().toLowerCase().contains(name)) {
                    Display.card(r.getDetails());
                    found = true;
                }
            }
            if (!found) Display.error("No riders matching: " + name);

        } else {
            Display.warn("Invalid option.");
        }
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────
    public void update(Scanner sc) {
        Display.section("Update Rider");
        String id = InputHelper.readString(sc, "Rider ID to update");
        Rider r = findById(id);
        if (r == null) {
            Display.error("Rider not found: " + id);
            return;
        }

        Display.card(r.getDetails());
        Display.blank();
        Display.menuItem("1", "Phone Number");
        Display.menuItem("2", "Email Address");
        Display.menuItem("3", "Payment Method");
        Display.menuItem("4", "Home Address");
        String choice = InputHelper.readChoice(sc);

        switch (choice) {
            case "1":
                r.setPhone(InputHelper.readString(sc, "New Phone"));
                Display.success("Phone updated.");
                break;
            case "2":
                r.setEmail(InputHelper.readString(sc, "New Email"));
                Display.success("Email updated.");
                break;
            case "3":
                Display.info(
                    "Options: CREDIT_CARD / CASH / PAYPAL / APPLE_PAY");
                r.setPaymentMethod(
                    InputHelper.readString(sc, "New Payment Method"));
                Display.success("Payment method updated.");
                break;
            case "4":
                r.setHomeAddress(InputHelper.readString(sc, "New Address"));
                Display.success("Address updated.");
                break;
            default:
                Display.warn("Invalid option.");
        }
    }

    // ── DELETE ───────────────────────────────────────────────────────────────
    public void delete(Scanner sc) {
        Display.section("Delete Rider");
        String id = InputHelper.readString(sc, "Rider ID to delete");
        Rider r = findById(id);
        if (r == null) {
            Display.error("Rider not found: " + id);
            return;
        }

        Display.card(r.getDetails());
        if (InputHelper.confirm(sc,
                "Permanently delete " + r.getName() + "?")) {
            riders.remove(r);
            Display.success("Rider deleted: " + r.getName());
        } else {
            Display.info("Delete cancelled.");
        }
    }

    // ── HELPERS ──────────────────────────────────────────────────────────────
    public Rider findById(String id) {
        for (Rider r : riders) {
            if (r.getId().equalsIgnoreCase(id)) return r;
        }
        return null;
    }

    public ArrayList<Rider> getRiders() { return riders; }
}