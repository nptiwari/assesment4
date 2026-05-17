package model;

import java.util.ArrayList;

/**
 * Rider - extends Person.
 * Tracks payment method, rating, address and trip history.
 */
public class Rider extends Person {

    public enum PaymentMethod { CREDIT_CARD, CASH, PAYPAL, APPLE_PAY }

    private double        riderRating;
    private PaymentMethod paymentMethod;
    private String        homeAddress;
    private double        totalSpent;
    private ArrayList<String> tripHistory;

    public Rider(String id, String name, String email, String phone,
                 double riderRating, String paymentStr,
                 String homeAddress, double totalSpent) {
        super(id, name, email, phone);
        this.riderRating = riderRating;
        this.homeAddress = homeAddress;
        this.totalSpent  = totalSpent;
        this.tripHistory = new ArrayList<>();
        try {
            this.paymentMethod = PaymentMethod.valueOf(
                paymentStr.toUpperCase().replace(" ", "_"));
        } catch (Exception e) {
            this.paymentMethod = PaymentMethod.CREDIT_CARD;
        }
    }

    /** Polymorphic - rider specific detail block */
    @Override
    public String getDetails() {
        String stars = Driver.renderStars(riderRating);
        return String.format(
            "  ID       : %-8s  Name    : %s%n" +
            "  Email    : %-25s  Phone   : %s%n" +
            "  Address  : %s%n" +
            "  Rating   : %s %.1f  |  Payment: %-14s  |  Total Spent: $%.2f  |  Trips: %d",
            getId(), getName(),
            getEmail(), getPhone(),
            homeAddress,
            stars, riderRating,
            paymentMethod.name().replace("_", " "),
            totalSpent, tripHistory.size()
        );
    }

    /** Add a completed trip to history */
    public void addTrip(String tripId, double fare) {
        tripHistory.add(tripId);
        totalSpent += fare;
    }

    /** Serialize to CSV line */
    public String toCsv() {
        return String.join(",",
            "RIDER", getId(), getName(), getEmail(), getPhone(),
            String.format("%.1f", riderRating),
            paymentMethod.name(),
            homeAddress,
            String.format("%.2f", totalSpent)
        );
    }

    // Getters and Setters
    public double        getRiderRating()     { return riderRating; }
    public PaymentMethod getPaymentMethod()   { return paymentMethod; }
    public String        getHomeAddress()     { return homeAddress; }
    public double        getTotalSpent()      { return totalSpent; }
    public ArrayList<String> getTripHistory() { return tripHistory; }

    public void setRiderRating(double r)  { this.riderRating = r; }
    public void setHomeAddress(String a)  { this.homeAddress = a; }
    public void setPaymentMethod(String p) {
        try {
            this.paymentMethod = PaymentMethod.valueOf(
                p.toUpperCase().replace(" ", "_"));
        } catch (Exception e) {
            System.out.println("  Invalid payment method, keeping existing.");
        }
    }
}