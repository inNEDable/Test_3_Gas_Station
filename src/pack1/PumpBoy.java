package pack1;

public class PumpBoy implements Runnable {


    private GasStation employer;

    public PumpBoy(GasStation employer) {
        this.employer = employer;
    }

    @Override
    public void run() {
        while (true){
            try {
                checkForCar();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkForCar() throws InterruptedException {


        synchronized (GasStation.pumpKey) {

            while (!employer.hasCarWaitingForFilling()){
                GasStation.pumpKey.wait();
            }

            Car currentCar = employer.getCarWaiting();
            fillUpCar(currentCar);
        }
    }

    private void fillUpCar (Car currentCar) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("The car of " + currentCar.getDriver().getName() + " has been filled with "
                + currentCar.getFuelNeeded() + " litters of " + currentCar.getGasType());

        double billToPay = currentCar.getGasType().getPrice() * currentCar.getFuelNeeded();
        currentCar.setIsFull(true);
        currentCar.getDriver().goToPay(billToPay);

        try {
            employer.gasPumps.get(currentCar.getGasPumpNumber()).take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
