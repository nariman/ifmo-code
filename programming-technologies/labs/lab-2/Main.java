/*
 * Nariman Safiulin (narimansafiulin)
 * File: Main.java
 * Created on: Mar 13, 2018
 */

import java.util.*;

public class Main {
    public static void main(String[] args) {
        //
        // Task #1.1
        //

        GenericItem first = new GenericItem();
        GenericItem second = new GenericItem();
        GenericItem third = new GenericItem();

        first.ID = 578080;
        first.name = "PUBG";
        first.price = 29.99f;

        second.ID = 281990;
        second.name = "Stellaris";
        second.price = 39.99f;

        third.ID = 379430;
        third.name = "Kingdom Come: Deliverance";
        third.price = 59.99f;

        first.printAll();
        second.printAll();
        third.printAll();

        //
        // Task #1.2
        //

        first.category = Category.FOOD;

        first.printAll();
        second.printAll();
        third.printAll();

        //
        // Task 2
        //

        FoodItem fourth = new FoodItem();
        fourth.ID = 1;
        fourth.name = "Burger";
        fourth.price = 2.99f;
        fourth.dateOfIncome = new Date(1521451156000L);
        fourth.expires = 1;

        TechnicalItem fifth = new TechnicalItem();
        fifth.ID = 2;
        fifth.name = "TV";
        fifth.price = 2000;
        fifth.warrantyTime = 720;

        for (GenericItem item : Arrays.asList(first, second, third, fourth, fifth)) {
            item.printAll();
        }
    }
}
