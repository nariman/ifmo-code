/*
 * Nariman Safiulin (narimansafiulin)
 * File: FoodItem.java
 * Created on: Mar 19, 2018
 */

import java.util.*;

public class FoodItem extends GenericItem {
    Date dateOfIncome;
    short expires;

    void printAll() {
        System.out.printf(
            "ID: %d, Name: %s, Price: %5.2f$, Category: %s, Date of Income: %s, Expires: %d\n",
            ID, name, price, category, dateOfIncome.toString(), expires
        );
    }
}
