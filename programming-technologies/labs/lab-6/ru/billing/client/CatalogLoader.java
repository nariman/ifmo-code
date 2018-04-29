/*
 * Nariman Safiulin (narimansafiulin)
 * File: Main.java
 * Created on: Mar 26, 2018
 */

package ru.billing.client;

import ru.billing.stocklist.ItemCatalog;
import ru.itmo.exceptions.CatalogLoadException;


interface CatalogLoader {
    public void load(ItemCatalog catalog) throws CatalogLoadException;
}
