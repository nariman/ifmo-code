/*
 * Nariman Safiulin (narimansafiulin)
 * File: Main.java
 * Created on: Mar 26, 2018
 */

package ru.billing.client;

import java.util.*;

import ru.billing.stocklist.*;


public class CatalogStubLoader implements CatalogLoader {
    public void load(ItemCatalog catalog) {
        GenericItem item1 = new GenericItem("Sony TV", 23000f, Category.GENERAL);
        FoodItem item2 = new FoodItem("Bread", 12f, null, new Date(), (short) 10);

        catalog.addItem(item1);
        catalog.addItem(item2);
    }
}