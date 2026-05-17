package model;

/**
 * SUV - extends Vehicle.
 * Represents UberXL service tier (6-seat SUV).
 */
public class SUV extends Vehicle {

    public SUV(String make, String model, String plateNo, int year) {
        super(make, model, plateNo, year);
    }

    @Override
    public String getServiceTier() {
        return "UberXL";
    }

    @Override
    public int getSeatCapacity() {
        return 6;
    }
}