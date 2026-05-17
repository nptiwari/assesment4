package model;

/**
 * Car — extends Vehicle.
 * Represents UberX service tier (standard 4-seat car).
 */
public class Car extends Vehicle {

    public Car(String make, String model, String plateNo, int year) {
        super(make, model, plateNo, year);
    }

    @Override
    public String getServiceTier() {
        return "UberX";
    }

    @Override
    public int getSeatCapacity() {
        return 4;
    }
}