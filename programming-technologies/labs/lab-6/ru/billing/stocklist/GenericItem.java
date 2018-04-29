/*
 * Nariman Safiulin (narimansafiulin)
 * File: GenericItem.java
 * Created on: Mar 13, 2018
 */

package ru.billing.stocklist;

import java.util.*;


public class GenericItem {
    static protected int currentID = 0;

    protected int ID;
    protected String name;
    protected float price;
    protected Category category = Category.GENERAL;
    protected ArrayList<GenericItem> analogs;

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
                if (item != null) {
                    item.printAll();
                }
            }
        }
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ArrayList<GenericItem> getAnalogs() {
        return analogs;
    }

    public void setAnalogs(ArrayList<GenericItem> analogs) {
        this.analogs = analogs;
    }
}
