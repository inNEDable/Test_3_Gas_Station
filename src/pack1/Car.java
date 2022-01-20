package pack1;

import java.util.Random;

public class Car {

    public enum GasType {
        PETROL(2.40), DIESEL(2.47), LPG(1.36);

        private double price;

        GasType(double price) {
            this.price = price;
        }

        public double getPrice() {
            return price;
        }
    }

    private Driver driver;
    private GasType gasType;
    private int fuelNeeded;

    public Car() {
        this.gasType = GasType.values()[new Random().nextInt(GasType.values().length)];
        this.fuelNeeded = new Random().nextInt(31)+10;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() {
        return driver;
    }

    public int getFuelNeeded() {
        return fuelNeeded;
    }

    public GasType getGasType() {
        return gasType;
    }
}
