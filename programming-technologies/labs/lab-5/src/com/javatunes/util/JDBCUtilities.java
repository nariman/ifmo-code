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

/**
 * A collection of methods to make the life of JDBC programmers easier.
 * Most of them are derived from the simpleselect.java examples in the 
 * JDBC documentation.
 */
public class JDBCUtilities
{
   /**
    * Displays DatabaseMetaData information.
    */
   public static void printDatabaseMetaData(DatabaseMetaData dbdata) 
   {
      try
      {
         System.out.println();
         System.out.println("Connected to:     " + dbdata.getURL());
         System.out.println("Connected as:     " + dbdata.getUserName());
         System.out.println("Driver name:      " + dbdata.getDriverName());
         System.out.println("Driver version:   " + dbdata.getDriverVersion());
         System.out.println("Database name:    " + dbdata.getDatabaseProductName());
         System.out.println("Database version: " + dbdata.getDatabaseProductVersion());
         System.out.println();
      }
      catch (SQLException sqle) 
      {
         JDBCUtilities.printSQLException(sqle);
      }
   }
   
   /**
    * Checks for and displays warnings.
    * Returns true if a warning existed.
    */
  	public static boolean checkForWarnings(SQLWarning warning)
   {
      boolean rc = false;
      // if a SQLWarning object was given, display the warning messages
      // note that there could be multiple warnings chained together
      if (warning != null)
      {
         System.out.println("\n*** Warning ***\n");
         rc = true;
         while (warning != null)
         {
            System.out.println("Message:     " + warning.getMessage());
            System.out.println("SQLState:    " + warning.getSQLState());
            System.out.println("Vendor code: " + warning.getErrorCode());
            System.out.println();
            warning = warning.getNextWarning();
         }
      }
      return rc;
   }
   
   /** 
    * Displays SQLException information.
    */
  	public static void printSQLException(SQLException sqle)
   {
      sqle.printStackTrace();
      System.out.println();
      
      // multiple SQLExceptions can be chained together
      while (sqle != null)
      {
         System.out.println("Message:     " + sqle.getMessage());
         System.out.println("SQLState:    " + sqle.getSQLState());
         System.out.println("Vendor code: " + sqle.getErrorCode());
         System.out.println();
         sqle = sqle.getNextException();
      }
   }
   
   /**
    * Displays the contents of a ResultSet.
    */
  	public static void printResultSet(ResultSet rs)
  	throws SQLException
   {
      int i;
      // get the ResultSetMetaData - used for the column headings
      ResultSetMetaData rsmd = rs.getMetaData();
      
      // get the number of columns in the result set
      int numCols = rsmd.getColumnCount();
      
      // display column headings
      for (i = 1; i <= numCols; i++)
      {
         if (i > 1) System.out.print(",");
         System.out.print(rsmd.getColumnLabel(i));
      }
      System.out.println();
      
      // display data, fetching until end of the result set
      while (rs.next())
      {
         // loop through each column, get the column data, and display
         for (i = 1; i <= numCols; i++)
         {
            if (i > 1) System.out.print(",");
            System.out.print(rs.getString(i));
         }
         System.out.println();
      }
   }
}
