package pack1;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GasStation {

    public static final int PUMP_BOYS = 2;
    public static final int CASHIERS = 2;
    public static final int GAS_PUMPS = 5;
    public static final int CASH_REGISTERS = 2;
    public static final Object pumpKey = new Object();
    public static final Object [] cashRegisterKeys = new Object[CASHIERS];


    ////////
    protected HashMap<Integer, BlockingQueue<Car>> gasPumps;
    private List<PumpBoy> pumpBoys;

    //////////
    protected HashMap<Integer, BlockingQueue<Driver>> cashRegisters;
    private List<Cashier> cashiers;

    public GasStation() {
        this.gasPumps = new HashMap<>();
        for (int i = 0; i < GAS_PUMPS; i++) {
            gasPumps.put(i, new LinkedBlockingQueue<>());
        }
        this.pumpBoys = new ArrayList<>();
        for (int i = 0; i < PUMP_BOYS; i++) {
            pumpBoys.add(new PumpBoy(this));
        }
        
        this.cashRegisters = new HashMap<>();
        for (int i = 0; i < CASH_REGISTERS; i++) {
            cashRegisters.put(i, new LinkedBlockingQueue<>());
        }

        for (int i = 0; i < CASHIERS; i++) {
            cashRegisterKeys[i] = new Object();
        }

        this.cashiers = new ArrayList<>();
        for (int i = 0; i < CASHIERS; i++) {
            cashiers.add(new Cashier(this, i));
        }


        startAllWorkers();
    }

    public void enterInLineForCashRegister(Driver driver) {
        int cashRegisterNumber = new Random().nextInt(CASH_REGISTERS);

        try {
            cashRegisters.get(cashRegisterNumber).put(driver);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (GasStation.cashRegisterKeys[cashRegisterNumber]){
            GasStation.cashRegisterKeys[cashRegisterNumber].notifyAll();
        }
    }

    private void startAllWorkers() {
        for (PumpBoy pumpBoy : pumpBoys) {
            Thread thread = new Thread(pumpBoy);
            thread.start();
        }

        for (Cashier cashier : cashiers) {
            Thread thread = new Thread(cashier);
            thread.start();
        }
    }

    public void enterGasStation (Car car){
        synchronized (pumpKey) {
            int pumpNumber = new Random().nextInt(4)+1;
            try {
                gasPumps.get(pumpNumber).put(car);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            car.setGasPumpNumber(pumpNumber);

            pumpKey.notifyAll();
            car.getDriver().setGasStation(this);
        }

    }

    protected Car getCarWaiting(){

        for (Integer pumpNumber: gasPumps.keySet()){
            if (!gasPumps.get(pumpNumber).isEmpty() && !gasPumps.get(pumpNumber).peek().isFull()) {
                System.out.println("Car from pump NUMBER " + pumpNumber + " has been MARKED for filling");
                return gasPumps.get(pumpNumber).peek();
            }
        }
        return null;
    }

    public boolean hasCarWaitingForFilling() {
        for (Integer pumpNumber : gasPumps.keySet()){
            if (!gasPumps.get(pumpNumber).isEmpty() && !gasPumps.get(pumpNumber).peek().isFull()){
                return true;
            }
        }
        return false;
    }
}
