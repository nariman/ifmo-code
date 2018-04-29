/*
 * Nariman Safiulin (narimansafiulin)
 * File: ItemDAOMain.java
 * Created on: Apr 2, 2018
 */

package com.javatunes;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

import com.javatunes.util.*;


public class ItemDAOMain {
    public static void main(String[] args) {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection conn = DriverManager.getConnection(
                "jdbc:derby://localhost:1527/JavaTunesDB", "GUEST", "password");

            ItemDAO idao = new ItemDAO(conn);


            //
            // Task #3
            //

            MusicItem mi1 = idao.searchById(1L);
            MusicItem mi100 = idao.searchById(100L);

            System.out.println(mi1);
            System.out.println(mi100);

            //
            // Task #4
            //

            Collection<MusicItem> miOf = idao.searchByKeyword("of");
            Collection<MusicItem> miGay = idao.searchByKeyword("Gay");

            System.out.println(miOf);
            System.out.println(miGay);

            //
            // Task #5
            //

            MusicItem item = new MusicItem(
                0L,
                "Zombie",
                "Bad Wolves",
                "2018-01-19",
                new BigDecimal("1.29"),
                new BigDecimal("1.29")
            );

            idao.create(item);

            Collection<MusicItem> zombies = idao.searchByKeyword("Zombie");
            System.out.println(zombies);


            idao.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
