package pack1;

public class Cashier implements Runnable{

    private GasStation employer;
    private Integer cashRegisterNumber;
    private final Object myOwnCashierKey;

    public Cashier(GasStation employer, Integer cashRegisterNumber) {
        this.employer = employer;
        this.cashRegisterNumber = cashRegisterNumber;
        myOwnCashierKey = GasStation.cashRegisterKeys[cashRegisterNumber];
    }

    @Override
    public void run() {
        while (true){
            try {
                serveClients();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void serveClients() throws InterruptedException {

        synchronized (myOwnCashierKey){
            while (!employer.cashRegisters.containsKey(cashRegisterNumber)|| employer.cashRegisters.get(cashRegisterNumber).isEmpty()){
                myOwnCashierKey.wait();
            }
            Driver currentDriver = employer.cashRegisters.get(cashRegisterNumber).poll();
            double billToPay = currentDriver.getCar().getGasType().getPrice() * currentDriver.getCar().getFuelNeeded();

            System.out.println(currentDriver.getName() + " has PAID " + billToPay + "  >>>>>>>>> ");

        }

    }
}
