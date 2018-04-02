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

        GenericItem first = new GenericItem("First", 2.99f, Category.PRINT);
        GenericItem second = new GenericItem("Second", 3.99f, first);
        FoodItem third = new FoodItem("Burger", 1.99f);

        first.printAll();
        second.printAll();
        third.printAll();

        // Task #3.1

        String line = "Конфеты 'Маска';45;120";
        String[] item_fld = line.split(";");

        FoodItem fromString = new FoodItem(item_fld[0],
                                           Float.parseFloat(item_fld[1]),
                                           Short.parseShort(item_fld[2]));

        fromString.printAll();
    }
}
