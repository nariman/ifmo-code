/*
 * Nariman Safiulin (narimansafiulin)
 * File: FoodItem.java
 * Created on: Mar 19, 2018
 */

import java.util.*;

public class FoodItem extends GenericItem {
    Date dateOfIncome;
    short expires;

    public FoodItem(String name) {
        super(name, 0f, Category.FOOD);
        this.dateOfIncome = new Date(0);
    }

    public FoodItem(String name, float price) {
        this(name);
        this.price = price;
    }

    public FoodItem(String name, float price, short expires) {
        this(name, price);

        this.expires = expires;
    }

    public FoodItem(String name, float price, FoodItem analog, Date date, short expires) {
        this(name, price, expires);

        this.dateOfIncome = date;

        this.analogs.add(analog);
    }

    void printAll() {
        System.out.printf(
            "ID: %d, Name: %s, Price: %5.2f$, Category: %s, Date of Income: %s, Expires: %d\n",
            ID, name, price, category, dateOfIncome.toString(), expires
        );

        if (this.analogs.size() > 0) {
            System.out.println("Analogs:");

            for (GenericItem item : this.analogs) {
                item.printAll();
            }
        }
    }
}
