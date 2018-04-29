/*
 * Nariman Safiulin (narimansafiulin)
 * File: U1901Bank.java
 * Created on: Apr 16, 2018
 */


public class U1901Bank {
    int intTo = 0;
    int intFrom = 220;

    public synchronized void calc(int intTransaction, long lngTimeout) {
        System.out.println("Current thread (before): " + Thread.currentThread().getName());
        this.intFrom -= intTransaction;

        try {
            Thread.sleep(lngTimeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.intTo += intTransaction;
        System.out.println("Current thread (after): " + Thread.currentThread().getName());
        System.out.println("Current values: " + this.intFrom + " : " + this.intTo);
    }
}
