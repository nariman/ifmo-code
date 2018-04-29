/*
 * Nariman Safiulin (narimansafiulin)
 * File: JDBCTest.java
 * Created on: Apr 2, 2018
 */

package com.javatunes;

import java.sql.*;


public class JDBCTest {
    public static void main(String[] args) {
        try {
            Connection conn = null;

            Class.forName("org.apache.derby.jdbc.ClientDriver");

            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/JavaTunesDB",
                                               "GUEST", "password");

            DatabaseMetaData dbmd = conn.getMetaData();

            System.out.println("Driver name: " + dbmd.getDriverName());
            System.out.println("User name: " + dbmd.getUserName());

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
