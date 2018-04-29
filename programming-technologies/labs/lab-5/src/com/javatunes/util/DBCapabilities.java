/*
 * This code is sample code, provided as-is, and we make no
 * warranties as to its correctness or suitablity for
 * any purpose.
 *
 * We hope that it's useful to you.  Enjoy.
 * Copyright 2004-8 LearningPatterns Inc.
 */

package com.javatunes.util;

import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;

class DBCapabilities
{
   public static void main(String[] args)
   {
      Connection conn = null;
      try
      {
         // create and load Properties object
         // the properties file is jdbc_build.properties
         Properties dbProps = new Properties();
         dbProps.load(new FileInputStream("jdbc_build.properties"));

         // read the properties (db.driver and db.url)
         String driver = dbProps.getProperty("db.driver");
         String url = dbProps.getProperty("db.url");
         
         // load the JDBC driver class
         Class.forName(driver);
         
         String user = dbProps.getProperty("db.user");
         if (user != null) { // connect to the database with user/passwd
            String passwd = dbProps.getProperty("db.passwd");
            conn = DriverManager.getConnection(url,user,passwd);
         }
         else {              // connect to the database with no user name
            conn = DriverManager.getConnection(url);
         }
         
         // get the metadata object
         DatabaseMetaData dbmd = conn.getMetaData();
         
         // print connection information
         System.out.println();
         System.out.println("Connected to:     " + dbmd.getURL());
         System.out.println("Connected as:     " + dbmd.getUserName());
         System.out.println("Driver name:      " + dbmd.getDriverName());
         System.out.println("Driver version:   " + dbmd.getDriverVersion());
         System.out.println("Database name:    " + dbmd.getDatabaseProductName());
         System.out.println("Database version: " + dbmd.getDatabaseProductVersion());
         System.out.println();
         
         // print ResultSet functionality         
         // is TYPE_SCROLL_INSENSITIVE + CONCUR_READ_ONLY supported?
         boolean scrollInsensitiveReadOnly = dbmd.supportsResultSetConcurrency(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

         // is TYPE_SCROLL_INSENSITIVE + CONCUR_UPDATABLE supported?
         boolean scrollInsensitiveUpdatable = dbmd.supportsResultSetConcurrency(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
         // is TYPE_SCROLL_SENSITIVE + CONCUR_READ_ONLY supported?
         boolean scrollSensitiveReadOnly = dbmd.supportsResultSetConcurrency(
            ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

         // is TYPE_SCROLL_SENSITIVE + CONCUR_UPDATABLE supported?
         boolean scrollSensitiveUpdatable = dbmd.supportsResultSetConcurrency(
            ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);            
            
         // are batch updates supported?
         boolean batchUpdates = dbmd.supportsBatchUpdates();
         
         
         // print report
         System.out.println("true means the feature is supported, false means it is not.");
         System.out.println("-----------------------------------------------------------");

         System.out.println("Scrollable, insensitive, read-only ResultSets: " + scrollInsensitiveReadOnly);
         System.out.println("Scrollable, insensitive, updatable ResultSets: " + scrollInsensitiveUpdatable);
         System.out.println();
        
         System.out.println("Scrollable, sensitive, read-only ResultSets:   " + scrollSensitiveReadOnly);
         System.out.println("Scrollable, sensitive, updatable ResultSets:   " + scrollSensitiveUpdatable);
         System.out.println();
         
         System.out.println("Batch updates:                                 " + batchUpdates);
         System.out.println();
      }
      catch (Exception e)
      {
         e.printStackTrace();  
      }
      finally
      {
         // close connection
         try
         {
            if (conn != null)
            {
               conn.close();
               System.out.println("Connection closed.");
            }
         }
         catch (SQLException ignored) { }
      }
   }
}
