/*
 * Nariman Safiulin (narimansafiulin)
 * File: Main.java
 * Created on: Mar 12, 2018
 */

/**
 * One another Hello World program on Java.
 */
public class Main {
    public static void main(String[] args) {
        //
        // Task #1
        //

        System.out.println("Starting project");

        //
        // Task #2
        //

        byte v_byte = 120;
        short v_short = 129;
        char v_char = 'a';
        int v_int = 65999;
        long v_long = 4294967296L;
        float v_float = 0.33333334F;
        double v_double = 0.3333333333333333;
        boolean v_boolean = true;

        System.out.println("v_byte = " + v_byte);
        System.out.println("v_short = " + v_short);
        System.out.println("v_char = " + v_char);
        System.out.println("v_int = " + v_int);
        System.out.println("v_long = " + v_long);
        System.out.println("v_float = " + v_float);
        System.out.println("v_double = " + v_double);
        System.out.println("v_boolean = " + v_boolean);

        //
        // Task #3
        //

        for (int i = 0; i < 26; i++) {
            System.out.print((char) ('a' + i));
        }

        System.out.println();

        long begin = new java.util.Date().getTime();
        long i = 0;

        while (i < 100000000) {
            i += 1;
        }

        long end = new java.util.Date().getTime();
        System.out.println("End - begin: " + (end - begin));

        //
        // Task #5.1
        //

        int[] mas = {12, 43, 12, -65, 778, 123, 32, 76};
        int max = Integer.MIN_VALUE;

        for (int j = 0; j < mas.length; j++) {
            if (mas[j] > max) {
                max = mas[j];
            }
        }

        System.out.println(max);
    }
}
