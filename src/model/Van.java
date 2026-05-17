package model;

/**
 * Van - extends Vehicle.
 * Represents UberVan service tier (8-seat van).
 */
public class Van extends Vehicle {

    public Van(String make, String model, String plateNo, int year) {
        super(make, model, plateNo, year);
    }

    @Override
    public String getServiceTier() {
        return "UberVan";
    }

    @Override
    public int getSeatCapacity() {
        return 8;
    }
}