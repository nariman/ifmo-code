/**
 * Nariman Safiulin (woofilee)
 * File: CheckedSubtract.java
 * Created on: Mar 27, 2016
 */

package expression.exceptions;

import expression.AbstractBinaryOperation;
import expression.TripleExpression;

public class CheckedSubtract extends AbstractBinaryOperation {

    public CheckedSubtract(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    private void assertSafeOperation(int left, int right) {
        if (right > 0
                ? left < Integer.MIN_VALUE + right
                : left > Integer.MAX_VALUE + right) {
            throw new ArithmeticException("[ERROR] Overflow: cannot to " +
                    "safely subtract " + left + "-" + right);
        }
    }

    @Override
    public int operate(int left, int right) {
        assertSafeOperation(left, right);
        return left - right;
    }
}
