/*
 * Nariman Safiulin (narimansafiulin)
 * File: GenericItem.java
 * Created on: Mar 13, 2018
 */

import java.util.*;

public class GenericItem {
    static int currentID = 0;

    public int ID;
    public String name;
    public float price;
    public Category category = Category.GENERAL;
    public ArrayList<GenericItem> analogs;

    public GenericItem() {
        this.ID = ++GenericItem.currentID;
        this.name = "Unknown";
        this.price = 0;
        this.analogs = new ArrayList<>();
    }

    public GenericItem(String name, float price) {
        this();

        this.name = name;
        this.price = price;
    }

    public GenericItem(String name, float price, Category category) {
        this(name, price);

        this.category = category;
    }

    public GenericItem(String name, float price, GenericItem analog) {
        this(name, price);

        this.analogs.add(analog);
    }

    void printAll() {
        System.out.printf("ID: %d, Name: %s, Price: %5.2f$, Category: %s\n", ID, name, price, category);

        if (analogs.size() > 0) {
            System.out.println("Analogs:");

            for (GenericItem item : analogs) {
                item.printAll();
            }
        }
    }
}
