/*
 * Nariman Safiulin (narimansafiulin)
 * File: Main.java
 * Created on: Mar 26, 2018
 */

package ru.billing.client;

import java.util.*;

import ru.billing.stocklist.*;
import ru.itmo.exceptions.*;


public class CatalogStubLoader implements CatalogLoader {
    public void load(ItemCatalog catalog) throws CatalogLoadException {
        GenericItem item1 = new GenericItem("Sony TV", 23000f, Category.GENERAL);
        FoodItem item2 = new FoodItem("Bread", 12f, null, new Date(), (short) 10);

        try {
            catalog.addItem(item1);
            catalog.addItem(item2);
        } catch (ItemAlreadyExistsException e) {
            e.printStackTrace();
            throw new CatalogLoadException();
        }
    }
}
