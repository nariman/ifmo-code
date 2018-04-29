/*
 * Nariman Safiulin (narimansafiulin)
 * File: U1901Thread.java
 * Created on: Apr 16, 2018
 */


public class U1901Thread extends Thread {
    U1901Bank bankWork;
    int intTrans;
    long lngSleep;

    public U1901Thread(U1901Bank bankWork, int intTrans, long lngSleep) {
        this.bankWork = bankWork;
        this.intTrans = intTrans;
        this.lngSleep = lngSleep;
    }

    public void run() {
        this.bankWork.calc(intTrans, lngSleep);
    }
}
