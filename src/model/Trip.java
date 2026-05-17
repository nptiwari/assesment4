package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Trip - links a Driver and Rider for a single ride.
 * Tracks full lifecycle: REQUESTED > IN_PROGRESS > COMPLETED | CANCELLED
 * Includes full fare breakdown and generates a printable receipt.
 */
public class Trip {

    public enum Status { REQUESTED, IN_PROGRESS, COMPLETED, CANCELLED }

    private static final double BASE_FARE       = 2.50;
    private static final double RATE_PER_KM     = 1.80;
    private static final double PEAK_MULTIPLIER = 1.3;
    private static final double PLATFORM_CUT    = 0.20;
    private static final DateTimeFormatter FMT  =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private String tripId;
    private String driverId;
    private String riderId;
    private String pickup;
    private String dropoff;
    private double distanceKm;
    private boolean peakHour;

    // Fare breakdown
    private double baseFare;
    private double distanceFare;
    private double subtotal;
    private double platformFee;
    private double driverEarning;
    private double totalFare;

    private Status        status;
    private LocalDateTime requestedAt;
    private LocalDateTime completedAt;

    // Post trip ratings
    private double riderRatingGiven;
    private double driverRatingGiven;

    public Trip(String tripId, String driverId, String riderId,
                String pickup, String dropoff,
                double distanceKm, boolean peakHour) {
        this.tripId      = tripId;
        this.driverId    = driverId;
        this.riderId     = riderId;
        this.pickup      = pickup;
        this.dropoff     = dropoff;
        this.distanceKm  = distanceKm;
        this.peakHour    = peakHour;
        this.status      = Status.REQUESTED;
        this.requestedAt = LocalDateTime.now();
        this.riderRatingGiven  = -1;
        this.driverRatingGiven = -1;
        calculateFare();
    }

    /** Calculate full fare breakdown */
    private void calculateFare() {
        this.baseFare      = BASE_FARE;
        this.distanceFare  = distanceKm * RATE_PER_KM;
        this.subtotal      = (baseFare + distanceFare) *
                             (peakHour ? PEAK_MULTIPLIER : 1.0);
        this.platformFee   = subtotal * PLATFORM_CUT;
        this.driverEarning = subtotal - platformFee;
        this.totalFare     = subtotal;
    }

    /** Trip lifecycle methods */
    public void startTrip()    {
        this.status = Status.IN_PROGRESS;
    }
    public void cancelTrip()   {
        this.status = Status.CANCELLED;
        completedAt = LocalDateTime.now();
    }
    public void completeTrip() {
        this.status = Status.COMPLETED;
        completedAt = LocalDateTime.now();
    }

    public void setRiderRating(double r)  { this.riderRatingGiven  = r; }
    public void setDriverRating(double r) { this.driverRatingGiven = r; }

    /** Generate formatted receipt */
    public String getReceipt() {
        String line = "─".repeat(46);
        return "\n" + line + "\n" +
               "           UBER RIDE RECEIPT\n" +
               line + "\n" +
               String.format("  Trip ID    : %s%n", tripId) +
               String.format("  Date/Time  : %s%n", requestedAt.format(FMT)) +
               String.format("  Driver ID  : %s%n", driverId) +
               String.format("  Rider ID   : %s%n", riderId) +
               line + "\n" +
               String.format("  From       : %s%n", pickup) +
               String.format("  To         : %s%n", dropoff) +
               String.format("  Distance   : %.1f km%s%n",
                   distanceKm, peakHour ? "  [PEAK HOUR]" : "") +
               line + "\n" +
               String.format("  Base Fare  : $%.2f%n", baseFare) +
               String.format("  Distance   : $%.2f  (%.1f km x $%.2f)%n",
                   distanceFare, distanceKm, RATE_PER_KM) +
               (peakHour ? String.format("  Peak x%.1f  : +$%.2f%n",
                   PEAK_MULTIPLIER,
                   subtotal - (baseFare + distanceFare)) : "") +
               line + "\n" +
               String.format("  TOTAL      : $%.2f%n", totalFare) +
               String.format("  Platform   : -$%.2f (%.0f%%)%n",
                   platformFee, PLATFORM_CUT * 100) +
               String.format("  Driver Pay : $%.2f%n", driverEarning) +
               line + "\n";
    }

    @Override
    public String toString() {
        return String.format("[%s] %-10s  %-18s > %-18s  %.1fkm  $%.2f  %s",
            tripId, status, pickup, dropoff,
            distanceKm, totalFare, driverId);
    }

    // Getters
    public String        getTripId()            { return tripId; }
    public String        getDriverId()          { return driverId; }
    public String        getRiderId()           { return riderId; }
    public String        getPickup()            { return pickup; }
    public String        getDropoff()           { return dropoff; }
    public double        getDistanceKm()        { return distanceKm; }
    public double        getTotalFare()         { return totalFare; }
    public double        getDriverEarning()     { return driverEarning; }
    public Status        getStatus()            { return status; }
    public double        getRiderRatingGiven()  { return riderRatingGiven; }
    public double        getDriverRatingGiven() { return driverRatingGiven; }
    public LocalDateTime getRequestedAt()       { return requestedAt; }
    public void          setDriverId(String d)  { this.driverId = d; }
}