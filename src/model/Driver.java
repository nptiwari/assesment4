package model;

import java.util.ArrayList;

/**
 * Driver - extends Person.
 * Tracks ratings, earnings, trip count, vehicle, and performance history.
 */
public class Driver extends Person {

    public enum Status { AVAILABLE, ON_TRIP, SUSPENDED, OFFLINE }

    private String licenseNo;
    private double rating;
    private double totalEarnings;
    private int tripsCompleted;
    private int warningCount;
    private String vehicleType;
    private String vehicleMake;
    private String vehicleModel;
    private String vehiclePlate;
    private int vehicleYear;
    private Status status;
    private ArrayList<Double> ratingHistory;

    public Driver(String id, String name, String email, String phone,
                  String licenseNo, double rating,
                  double totalEarnings, int tripsCompleted,
                  String vehicleType, String vehicleMake, String vehicleModel,
                  String vehiclePlate, int vehicleYear, String statusStr) {
        super(id, name, email, phone);
        this.licenseNo      = licenseNo;
        this.rating         = rating;
        this.totalEarnings  = totalEarnings;
        this.tripsCompleted = tripsCompleted;
        this.warningCount   = 0;
        this.vehicleType    = vehicleType;
        this.vehicleMake    = vehicleMake;
        this.vehicleModel   = vehicleModel;
        this.vehiclePlate   = vehiclePlate;
        this.vehicleYear    = vehicleYear;
        this.ratingHistory  = new ArrayList<>();
        try {
            this.status = Status.valueOf(statusStr.toUpperCase());
        } catch (Exception e) {
            this.status = Status.AVAILABLE;
        }
    }

    /** Polymorphic - driver specific detail block */
    @Override
    public String getDetails() {
        String stars = renderStars(rating);
        return String.format(
            "  ID       : %-8s  Name    : %s%n" +
            "  Email    : %-25s  Phone   : %s%n" +
            "  License  : %-15s  Status  : %s%n" +
            "  Vehicle  : %d %s %s (%s) - Plate: %s%n" +
            "  Rating   : %s %.1f  |  Trips: %d  |  Earnings: $%.2f",
            getId(), getName(),
            getEmail(), getPhone(),
            licenseNo, status,
            vehicleYear, vehicleMake, vehicleModel, vehicleType, vehiclePlate,
            stars, rating, tripsCompleted, totalEarnings
        );
    }

    /** Update rating average and add earnings after a trip */
    public void recordTrip(double fareEarned, double newRating) {
        ratingHistory.add(newRating);
        if (tripsCompleted > 0) {
            this.rating = (rating * tripsCompleted + newRating) / (tripsCompleted + 1);
        } else {
            this.rating = newRating;
        }
        this.rating        = Math.round(this.rating * 10.0) / 10.0;
        this.totalEarnings += fareEarned;
        this.tripsCompleted++;
    }

    /** Evaluate performance - apply bonus or penalty */
    public String evaluatePerformance() {
        if (status == Status.SUSPENDED) {
            return "  [SKIPPED] Driver is already suspended.";
        }
        if (rating >= 4.5) {
            double bonus = tripsCompleted >= 50 ? 100.0 : 50.0;
            totalEarnings += bonus;
            return String.format(
                "  [BONUS +$%.0f] Rating %.1f - Keep up the great work!", bonus, rating);
        } else if (rating >= 3.5) {
            return String.format(
                "  [OK] Rating %.1f - Performance acceptable.", rating);
        } else {
            warningCount++;
            if (warningCount >= 3) {
                status = Status.SUSPENDED;
                return String.format(
                    "  [SUSPENDED] Rating %.1f - 3 warnings reached.", rating);
            }
            return String.format(
                "  [WARNING %d/3] Rating %.1f - Improvement required.", warningCount, rating);
        }
    }

    /** Render star rating display */
    public static String renderStars(double rating) {
        int full = (int) Math.round(rating);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(i < full ? "*" : "-");
        }
        return sb.toString();
    }

    /** Serialize to CSV line */
    public String toCsv() {
        return String.join(",",
            "DRIVER", getId(), getName(), getEmail(), getPhone(),
            licenseNo,
            String.format("%.1f", rating),
            String.format("%.2f", totalEarnings),
            String.valueOf(tripsCompleted),
            vehicleType, vehicleMake, vehicleModel, vehiclePlate,
            String.valueOf(vehicleYear),
            status.name()
        );
    }

    // Getters and Setters
    public String getLicenseNo()           { return licenseNo; }
    public double getRating()              { return rating; }
    public double getTotalEarnings()       { return totalEarnings; }
    public int    getTripsCompleted()      { return tripsCompleted; }
    public int    getWarningCount()        { return warningCount; }
    public String getVehicleType()         { return vehicleType; }
    public String getVehicleMake()         { return vehicleMake; }
    public String getVehicleModel()        { return vehicleModel; }
    public String getVehiclePlate()        { return vehiclePlate; }
    public int    getVehicleYear()         { return vehicleYear; }
    public Status getStatus()              { return status; }
    public void   setStatus(Status s)      { this.status = s; }
    public void   setRating(double r)      { this.rating = r; }
    public void   setVehicleMake(String v) { this.vehicleMake  = v; }
    public void   setVehicleModel(String v){ this.vehicleModel = v; }
    public void   setVehiclePlate(String v){ this.vehiclePlate = v; }
    public void   setVehicleType(String v) { this.vehicleType  = v; }
}