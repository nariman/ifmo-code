/**
* Nariman Safiulin (woofilee)
* File: E.java
* Created on: Oct 19, 2015
*/

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Scanner;

public class Z
{
    public static void main(String[] args) throws Exception
    {
        PrintWriter pw = new PrintWriter(new File("z.out"));
        Scanner sc = new Scanner(new File("z.in"));

        pw.println(sc);
        /*
        (new PrintWriter(new File("a.out"))).println(
                (new Scanner(new File("a.in")).)
        );*/

        pw.close();
        sc.close();
    }
}
