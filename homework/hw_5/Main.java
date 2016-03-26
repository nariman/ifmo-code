/**
 * Nariman Safiulin (woofilee)
 * File: Main.java
 * Created on: Mar 27, 2016
 */

import expression.*;

public class Main {
    public static void main(String[] args) {
        ExpressionObject exp =
                new Add(
                        new Subtract(
                                new Multiply(
                                        new Variable("x"),
                                        new Variable("x")
                                ),
                                new Multiply(
                                        new Const(2),
                                        new Variable("x")
                                )
                        ),
                        new Const(1)
                );

        try {
            System.out.print(exp.evaluate(Integer.parseInt(args[0])));
        } catch (NumberFormatException e) {
            System.out.print(exp.evaluate(Double.parseDouble(args[0])));
        }
    }
}
