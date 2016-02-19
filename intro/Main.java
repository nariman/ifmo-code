import java.io.*;
import java.security.MessageDigest;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {
       try {
           pw.write("Hello world!");
       } catch (FileNotFoundException e) {
           System.out.println("File not found!");
           e.printStackTrace();
       } catch (Exception e) {
           return;
       } finally {
           pw.close();
       }
    }

   public static class Pair implements Comparable<Pair>
   {
       String name;
       int children;

       public Pair(String name, int children)
       {
           this.name = name;
           this.children = children;
       }

       @Override
       public int compareTo(Pair o)
       {
           return this.children - o.children;
       }
   }

   public static void main(String[] args)
   {
       Scanner sc = new Scanner(System.in);
       int n = sc.nextInt();

       Pair[] a = new Pair[n];

       for (int i = 0; i < n; ++i)
       {
           a[i] = new Pair(sc.next(), sc.nextInt());
       }

       Arrays.sort(a);

       for (int i = 0; i < n; ++i)
       {
           System.out.println(a[i].name + " " + a[i].children);
       }
   }
}
