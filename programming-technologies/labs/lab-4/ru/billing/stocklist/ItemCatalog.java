/*
 * Nariman Safiulin (narimansafiulin)
 * File: Main.java
 * Created on: Mar 26, 2018
 */

package ru.billing.stocklist;

import java.util.*;


public class ItemCatalog {
    private HashMap<Integer, GenericItem> catalog = new HashMap<>();
    private ArrayList<GenericItem> ALCatalog = new ArrayList<>();

    public void addItem(GenericItem item) {
        catalog.put(item.ID, item);
        ALCatalog.add(item);
    }

    public void printItems() {
        for (GenericItem item : ALCatalog) {
            item.printAll();
        }
    }

    public GenericItem findItemByID(int id) {
        if (!catalog.containsKey(id)) {
            return null;
        } else {
            return catalog.get(id);
        }
    }

    public GenericItem findItemByIDAL(int id) {
        for (GenericItem item : ALCatalog) {
            if (item.ID == id) {
                return item;
            }
        }

        return null;
    }
}