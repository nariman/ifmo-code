/*
 * Nariman Safiulin (narimansafiulin)
 * File: CatalogFileLoader.java
 * Created on: Apr 16, 2018
 */

package ru.billing.client;

import java.io.*;
import java.util.*;

import ru.billing.stocklist.*;
import ru.itmo.exceptions.*;


public class CatalogFileLoader implements CatalogLoader {
    private String fileName;

    public CatalogFileLoader(String fileName) {
        this.fileName = fileName;
    }

    public void load(ItemCatalog catalog) throws CatalogLoadException {
        File f = new File(fileName);
        FileInputStream fis;
        String line;

        try {
            fis = new FileInputStream(f);
            Scanner s = new Scanner(fis);

            while(s.hasNextLine()) {
                line = s.nextLine();
                if (line.length() == 0) break;

                String[] item_fld = line.split(";");
                String name = item_fld[0];
                float price = Float.parseFloat(item_fld[1]);
                short expires = Short.parseShort(item_fld[2]);
                FoodItem item = new FoodItem(name, price, null, new Date(), expires);
                catalog.addItem(item);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new CatalogLoadException();
        } catch (ItemAlreadyExistsException e) {
            e.printStackTrace();
            throw new CatalogLoadException();
        }
    }
}
