package pack1;

public class Driver {
   private final Car car;
   private String name;
   private GasStation gasStation;

    public Driver(String name) {
        this.car = new Car();
        this.car.setDriver(this);
        this.name = name;
    }

    public Car getCar() {
        return car;
    }

    public String getName() {
        return name;
    }

    public void goToPay(double billToPay) {
        gasStation.enterInLineForCashRegister(this);
    }

    public void setGasStation(GasStation gasStation) {
        this.gasStation = gasStation;
    }
}
