/**
 * Nariman Safiulin (woofilee)
 * File: Main.java
 * Created on: Mar 27, 2016
 */

import expression.*;
import expression.exceptions.ExceptionsTest;
import expression.exceptions.ExpressionParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;


public class Main {
//    static int maxIterations = 0;

    private static int sqrt(int value) {
        if (value == 0) {
            return 0;
        }

        int div, res = value;
//        int iters = 0;

        if ((value & 4294901760L) > 0)
            if ((value & 4278190080L) > 0)
                div = 16383;
            else
                div = 1023;
        else if ((value & 65280) > 0)
            div = 63;
        else
            div = (value > 4) ? 7 : value;

        while (true) {
//            iters++;
//            maxIterations = Math.max(maxIterations, iters);
            div = (value / div + div) >> 1;
            if (res > div) {
                res = div;
            } else {
                return res;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(new BufferedReader(new FileReader("test.txt")));
        ExpressionParser ep = new ExpressionParser();
        TripleExpression te = ep.parse(in.nextLine());

        System.out.println(te.evaluate(1, 2, 3));

//        Random random = new Random();
//        int[] ints = new int[1 << 24];
//        double[] doubles = new double[1 << 24];
//        long startTime, endTime;
//
//        for (int i = 0; i < (1 << 24); i++) {
//            ints[i] = random.nextInt(Integer.MAX_VALUE);
//            doubles[i] = (double) ints[i];
//        }
//
//        startTime = System.currentTimeMillis();
//        for (int i = 0; i < (1 << 24); i++) {
//            Math.sqrt(doubles[i]);
//        }
//        endTime = System.currentTimeMillis();
//
//        System.out.println("Total execution time of Math.sqrt: " + (endTime - startTime) + " millis.");
//
//        startTime = System.currentTimeMillis();
//        for (int i = 0; i < (1 << 24); i++) {
//            sqrt(ints[i]);
//        }
//        endTime = System.currentTimeMillis();
//
//        System.out.println("Total execution time of own sqrt: " + (endTime - startTime) + " millis.");
//        System.out.println("Max iterations - " + maxIterations);

//        String exp = "(2 * 2 / 2 + -1 + 2) * 3";
//        ExpressionParser ep = new ExpressionParser();
//        TripleExpression te = ep.parse(exp);
//        System.out.println(te.evaluate(0, 0, 0));

//        ExpressionObject exp =
//                new CheckedAdd(
//                        new CheckedSubtract(
//                                new CheckedMultiply(
//                                        new CheckedVariable("x"),
//                                        new CheckedVariable("x")
//                                ),
//                                new CheckedMultiply(
//                                        new CheckedConst(2),
//                                        new CheckedVariable("x")
//                                )
//                        ),
//                        new CheckedConst(1)
//                );
//        try {
//            System.out.print(exp.evaluate(Integer.parseInt(args[0])));
//        } catch (NumberFormatException e) {
//            System.out.print(exp.evaluate(Double.parseDouble(args[0])));
//        }
//        ExpressionParser ep = new ExpressionParser();
//        try {
//            ep.parse("10");
//        } catch (ParseException e) {
//            System.out.println("ParserException handled" + e.getMessage());
//            e.printStackTrace();
//        }
    }
}
