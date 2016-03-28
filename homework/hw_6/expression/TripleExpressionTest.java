package expression;

import static expression.Util.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class TripleExpressionTest {
    public static void main(final String[] args) {
        checkAssert(TripleExpressionTest.class);
        testExpression("10", new CheckedConst(10), (x, y, z) -> 10);
        testExpression("x", new CheckedVariable("x"), (x, y, z) -> x);
        testExpression("y", new CheckedVariable("y"), (x, y, z) -> y);
        testExpression("z", new CheckedVariable("z"), (x, y, z) -> z);
        testExpression("x+2", new CheckedAdd(new CheckedVariable("x"), new CheckedConst(2)), (x, y, z) -> x + 2);
        testExpression("2-y", new CheckedSubtract(new CheckedConst(2), new CheckedVariable("y")), (x, y, z) -> 2 - y);
        testExpression("3*z", new CheckedMultiply(new CheckedConst(3), new CheckedVariable("z")), (x, y, z) -> 3 * z);
        testExpression("x/-2", new CheckedDivide(new CheckedVariable("x"), new CheckedConst(-2)), (x, y, z) -> -x / 2);
        testExpression(
                "x*y+(z-1)/10",
                new CheckedAdd(
                        new CheckedMultiply(new CheckedVariable("x"), new CheckedVariable("y")),
                        new CheckedDivide(new CheckedSubtract(new CheckedVariable("z"), new CheckedConst(1)), new CheckedConst(10))
                ),
                (x, y, z) -> x * y + (z - 1) / 10
        );
        System.out.println("OK");
    }

    private static void testExpression(final String description, final TripleExpression actual, final TripleExpression expected) {
        System.out.println("Testing " + description);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    assertEquals(String.format("f(%d, %d, %d)", i, j, k), actual.evaluate(i, j, k), expected.evaluate(i, j, k));
                }
            }
        }
    }
}
