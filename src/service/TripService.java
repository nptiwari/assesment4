package service;

import model.Driver;
import model.Rider;
import model.Trip;
import util.Display;
import util.InputHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * TripService - manages the full trip lifecycle.
 *
 * Why Queue<Trip>?
 * Trip requests must be served in the order they arrive (FIFO).
 * Queue.offer() and Queue.poll() are O(1) with a LinkedList backend,
 * vs O(n) if we removed from the front of an ArrayList.
 * A Queue also communicates intent clearly - trips wait in a queue
 * and are dispatched one by one to available drivers.
 */
public class TripService {

    private Queue<Trip>     tripQueue;
    private ArrayList<Trip> activeTrips;
    private ArrayList<Trip> completedTrips;
    private ArrayList<Trip> cancelledTrips;
    private int             tripCounter;

    public TripService() {
        this.tripQueue      = new LinkedList<>();
        this.activeTrips    = new ArrayList<>();
        this.completedTrips = new ArrayList<>();
        this.cancelledTrips = new ArrayList<>();
        this.tripCounter    = 1;
    }

    // ── REQUEST TRIP ─────────────────────────────────────────────────────────
    public void requestTrip(Scanner sc, ArrayList<Rider> riders) {
        Display.section("Request a Trip");

        String riderId = InputHelper.readString(sc, "Rider ID");
        Rider rider = null;
        for (Rider r : riders) {
            if (r.getId().equalsIgnoreCase(riderId)) {
                rider = r;
                break;
            }
        }
        if (rider == null) {
            Display.error("Rider not found: " + riderId);
            return;
        }

        Display.info("Rider: " + rider.getName() +
            " | Payment: " + rider.getPaymentMethod());

        String pickup   = InputHelper.readString(sc, "Pickup Location");
        String dropoff  = InputHelper.readString(sc, "Dropoff Location");
        double distance = InputHelper.readPositiveDouble(sc,
            "Estimated Distance (km)");

        System.out.print("  Is this a peak hour trip? (yes/no): ");
        boolean peak = sc.nextLine().trim().equalsIgnoreCase("yes");

        String tripId = "T" + String.format("%03d", tripCounter++);
        Trip trip = new Trip(tripId, "UNASSIGNED", riderId,
            pickup, dropoff, distance, peak);
        tripQueue.offer(trip);

        Display.blank();
        Display.success("Trip " + tripId + " added to queue!");
        System.out.println(trip.getReceipt());
        Display.info("Trips currently in queue: " + tripQueue.size());
    }

    // ── ASSIGN NEXT TRIP ─────────────────────────────────────────────────────
    public void assignNextTrip(Scanner sc,
            ArrayList<Driver> drivers,
            ArrayList<Rider> riders) {
        Display.section("Assign Next Trip");

        if (tripQueue.isEmpty()) {
            Display.info("No trips waiting in queue.");
            return;
        }

        // Find first available driver
        Driver available = null;
        for (Driver d : drivers) {
            if (d.getStatus() == Driver.Status.AVAILABLE) {
                available = d;
                break;
            }
        }
        if (available == null) {
            Display.warn("No available drivers at this time.");
            return;
        }

        Trip trip = tripQueue.poll();
        trip.setDriverId(available.getId());
        trip.startTrip();
        available.setStatus(Driver.Status.ON_TRIP);

        Display.success("Trip " + trip.getTripId() +
            " assigned to " + available.getName());
        Display.info("Route: " + trip.getPickup() +
            " > " + trip.getDropoff());
        Display.info("Estimated fare: $" +
            String.format("%.2f", trip.getTotalFare()));
        Display.blank();

        if (InputHelper.confirm(sc, "Mark trip as completed now?")) {
            trip.completeTrip();
            available.setStatus(Driver.Status.AVAILABLE);
            available.recordTrip(trip.getDriverEarning(), 0);

            // Find rider and update
            for (Rider r : riders) {
                if (r.getId().equalsIgnoreCase(trip.getRiderId())) {
                    r.addTrip(trip.getTripId(), trip.getTotalFare());
                    break;
                }
            }

            System.out.println(trip.getReceipt());

            // Post trip ratings
            Display.subHeader("Post-Trip Ratings");
            double driverRating = InputHelper.readDouble(sc,
                "Rider's rating for driver (1-5)", 1.0, 5.0);
            double riderRating  = InputHelper.readDouble(sc,
                "Driver's rating for rider (1-5)", 1.0, 5.0);

            trip.setRiderRating(driverRating);
            trip.setDriverRating(riderRating);

            // Update driver rating
            int trips = available.getTripsCompleted();
            if (trips > 0) {
                double newRating = ((available.getRating() *
                    (trips - 1)) + driverRating) / trips;
                available.setRating(
                    Math.round(newRating * 10.0) / 10.0);
            }

            completedTrips.add(trip);
            Display.success("Trip completed! Driver earned: $" +
                String.format("%.2f", trip.getDriverEarning()));
        } else {
            activeTrips.add(trip);
            Display.info("Trip marked as in progress.");
        }
    }

    // ── CANCEL TRIP ──────────────────────────────────────────────────────────
    public void cancelQueuedTrip(Scanner sc) {
        Display.section("Cancel a Queued Trip");
        if (tripQueue.isEmpty()) {
            Display.info("No trips in queue to cancel.");
            return;
        }

        viewQueue();
        String tripId = InputHelper.readString(sc, "Trip ID to cancel");
        ArrayList<Trip> temp = new ArrayList<>(tripQueue);
        tripQueue.clear();
        boolean found = false;

        for (Trip t : temp) {
            if (t.getTripId().equalsIgnoreCase(tripId)) {
                t.cancelTrip();
                cancelledTrips.add(t);
                found = true;
                Display.success("Trip " + tripId + " cancelled.");
            } else {
                tripQueue.offer(t);
            }
        }
        if (!found) Display.error("Trip not found in queue: " + tripId);
    }

    // ── VIEW QUEUE ───────────────────────────────────────────────────────────
    public void viewQueue() {
        Display.section("Trip Queue  (" + tripQueue.size() + " waiting)");
        if (tripQueue.isEmpty()) {
            Display.info("Queue is empty.");
            return;
        }
        int pos = 1;
        for (Trip t : tripQueue) {
            System.out.printf("  #%-3d %s%n", pos++, t);
        }
    }

    // ── VIEW COMPLETED ───────────────────────────────────────────────────────
    public void viewCompleted() {
        Display.section("Completed Trips  (" +
            completedTrips.size() + ")");
        if (completedTrips.isEmpty()) {
            Display.info("No completed trips yet.");
            return;
        }
        for (Trip t : completedTrips) {
            System.out.println("  " + t);
        }
    }

    // ── REQUEST TRIP (GUI VERSION) ───────────────────────────────────────────
    // This allows the GUI to bypass the Scanner and pass data directly
    public void requestTripFromGUI(String riderId, String pickup, String dropoff, double distance, boolean peak) {
        String tripId = "T" + String.format("%03d", tripCounter++);
        Trip trip = new Trip(tripId, "UNASSIGNED", riderId, pickup, dropoff, distance, peak);
        tripQueue.offer(trip);
    }
    
    // Quick getter so the GUI can see how many trips are waiting
    public int getQueueSize() {
        return tripQueue.size();
    }

    // ── TRIP STATS ───────────────────────────────────────────────────────────
    public void showTripStats() {
        double totalRevenue = 0;
        for (Trip t : completedTrips) {
            totalRevenue += t.getTotalFare();
        }
        Display.stat("Trips completed",
            String.valueOf(completedTrips.size()));
        Display.stat("Trips in queue",
            String.valueOf(tripQueue.size()));
        Display.stat("Trips active",
            String.valueOf(activeTrips.size()));
        Display.stat("Trips cancelled",
            String.valueOf(cancelledTrips.size()));
        Display.stat("Total revenue",
            String.format("$%.2f", totalRevenue));
        Display.stat("Platform earnings",
            String.format("$%.2f", totalRevenue * 0.20));
    }

    public ArrayList<Trip> getCompletedTrips() {
        return completedTrips;
    }
}