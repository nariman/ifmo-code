/**
 * Nariman Safiulin (woofilee)
 * File: Main.java
 * Created on: Mar 14, 2016
 */

public class Main {
    public static void main(String[] args) {
        Add exp = new Add(
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

        System.out.println(exp.evaluate(Integer.parseInt(args[0])));
    }
}
