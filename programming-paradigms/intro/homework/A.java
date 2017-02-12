/**
* Nariman Safiulin (woofilee)
* File: A.java
* Created on: Nov 04, 2015
*/

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class A
{
    public static void main(String[] args) throws Exception
    {
        Scanner sc = new Scanner(new File("a.in"));
        PrintWriter pw = new PrintWriter(new File("a.out"));
        long sum;

        while (sc.hasNextLine())
        {
            sum = 0;
            Scanner scLine = new Scanner(sc.nextLine());
            while (scLine.hasNext())
            {
                sum += scLine.nextInt();
            }
            scLine.close();
            pw.println(sum);
        }

        sc.close();
        pw.close();
    }
}
