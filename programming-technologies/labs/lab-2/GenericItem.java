/*
 * Nariman Safiulin (narimansafiulin)
 * File: GenericItem.java
 * Created on: Mar 13, 2018
 */

public class GenericItem {
    public int ID;
    public String name;
    public float price;
    public Category category = Category.GENERAL;

    void printAll() {
        System.out.printf("ID: %d, Name: %s, Price: %5.2f$, Category: %s\n", ID, name, price, category);
    }
}
