package pack1;

public class PumpBoy implements Runnable {


    private GasStation employee;

    public PumpBoy(GasStation employee) {
        this.employee = employee;
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

            while (!employee.hasCarsWaiting()){
                GasStation.pumpKey.wait();
            }

            Car currentCar = employee.getCarWaiting();
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

        currentCar.getDriver().goToPay(billToPay);
    }
}
