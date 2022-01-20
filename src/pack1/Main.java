package pack1;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        GasStation gasStation = new GasStation();
        ArrayList<Driver> drivers = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Driver driver = new Driver("Drive " + (i+1));
            drivers.add(driver);
            gasStation.enterGasStation(driver.getCar());
        }


    }
}
