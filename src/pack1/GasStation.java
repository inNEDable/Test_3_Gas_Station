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
            cashRegisters.put(i+1, new LinkedBlockingQueue<>());
        }
        this.cashiers = new ArrayList<>();
        for (int i = 0; i < CASHIERS; i++) {
            cashiers.add(new Cashier());
        }

        startAllWorkers();
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

            pumpKey.notifyAll();
            car.getDriver().setGasStation(this);
        }

    }
    
    protected Car getCarWaiting(){

        for (Integer pump: gasPumps.keySet()){
            if (!gasPumps.get(pump).isEmpty()) {
                try {
                    return gasPumps.get(pump).take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public boolean hasCarsWaiting() {
        for (Integer pumpNumber: gasPumps.keySet()){
            if (gasPumps.get(pumpNumber).size() > 0){
                return true;
            }
        }
        return false;
    }
}
