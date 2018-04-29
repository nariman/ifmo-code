/*
 * Nariman Safiulin (narimansafiulin)
 * File: Main.java
 * Created on: Mar 26, 2018
 */

package ru.billing.client;

import java.util.*;

import ru.billing.stocklist.*;


public class Main {
    public static void main(String[] args) {
        //
        // Task #1.1
        //

        ItemCatalog catalog = new ItemCatalog();

        GenericItem first = new GenericItem("PUBG", 29.99f);
        GenericItem second = new GenericItem("Stellaris", 39.99f);
        GenericItem third = new GenericItem("Kingdom Come: Deliverance", 59.99f);
        GenericItem fourth = new GenericItem("First", 2.99f, Category.PRINT);
        GenericItem fifth = new GenericItem("Second", 3.99f, fourth);
        FoodItem sixth = new FoodItem("Burger", 1.99f);
        FoodItem seventh = new FoodItem("Pepsi", 1.49f, sixth);
        FoodItem eighth = new FoodItem("Winner Winner Chicken Dinner", 9.99f);
        FoodItem ninth = new FoodItem("Coca-Cola", 1.49f, sixth);
        GenericItem tenth = new GenericItem("T-Shirt", 15.00f);

        // Copy-Paste, Copy-Paste!!1

        catalog.addItem(first);
        catalog.addItem(second);
        catalog.addItem(third);
        catalog.addItem(fourth);
        catalog.addItem(fifth);
        catalog.addItem(sixth);
        catalog.addItem(seventh);
        catalog.addItem(eighth);
        catalog.addItem(ninth);
        catalog.addItem(tenth);

        catalog.printItems();

        long begin = new Date().getTime();

        for (int i = 0; i < 100000; i++) {
            catalog.findItemByID(i);
        }

        long end = new Date().getTime();
        System.out.println("In HashMap: " + (end - begin));

        begin = new Date().getTime();

        for (int i = 0; i < 100000; i++) {
            catalog.findItemByIDAL(i);
        }

        end = new Date().getTime();

        System.out.println("In ArrayList: " + (end - begin));

        //
        // Task 2.1
        //

        CatalogLoader loader = new CatalogStubLoader();
        loader.load(catalog);

        catalog.printItems();
    }
}
