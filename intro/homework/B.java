/**
 * Created by WooFi on 19.10.2015.
 */

import java.io.*;
import java.util.Scanner;

public class B
{
    public static void main(String[] args) throws Exception
    {
        Scanner sc = new Scanner(new File("b.in"));
        PrintWriter pw = new PrintWriter(new File("b.out"));
        long sum = 0;
        StringBuilder str = new StringBuilder("");

        while (sc.hasNext())
        {
            if (sc.hasNextInt())
            {
                sum += sc.nextInt();
            }
            else
            {
                str.append(sc.next());
            }
        }

        pw.println(sum);
        pw.println(str.toString());
        sc.close();
        pw.close();
    }
}
