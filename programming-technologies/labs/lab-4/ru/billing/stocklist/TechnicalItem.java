/*
 * Nariman Safiulin (narimansafiulin)
 * File: TechnicalItem.java
 * Created on: Mar 19, 2018
 */

package ru.billing.stocklist;


public class TechnicalItem extends GenericItem {
    protected short warrantyTime;

    void printAll() {
        System.out.printf(
            "ID: %d, Name: %s, Price: %5.2f$, Category: %s, Warranty Time: %d\n",
            ID, name, price, category, warrantyTime
        );
    }

    public short getWarrantyTime() {
        return warrantyTime;
    }

    public void setWarrantyTime(short warrantyTime) {
        this.warrantyTime = warrantyTime;
    }
}
