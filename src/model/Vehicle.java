package model;

/**
 * Vehicle - Abstract base class.
 * Parent class for Car, SUV and Van.
 * Demonstrates abstraction.
 */
public abstract class Vehicle {

    private String make;
    private String model;
    private String plateNo;
    private int    year;

    public Vehicle(String make, String model, String plateNo, int year) {
        this.make    = make;
        this.model   = model;
        this.plateNo = plateNo;
        this.year    = year;
    }

    /** Abstract methods - subclasses must implement these */
    public abstract String getServiceTier();
    public abstract int    getSeatCapacity();

    /** Returns formatted vehicle information */
    public String getVehicleInfo() {
        return String.format("%s | %d %s %s | Plate: %s | Seats: %d",
            getServiceTier(), year, make, model,
            plateNo, getSeatCapacity());
    }

    // Getters
    public String getMake()    { return make; }
    public String getModel()   { return model; }
    public String getPlateNo() { return plateNo; }
    public int    getYear()    { return year; }
}