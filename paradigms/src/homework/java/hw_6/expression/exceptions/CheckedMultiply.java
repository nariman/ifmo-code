/**
 * Nariman Safiulin (woofilee)
 * File: CheckedMultiply.java
 * Created on: Mar 27, 2016
 */

package expression.exceptions;

import expression.AbstractBinaryOperation;
import expression.TripleExpression;

public class CheckedMultiply extends AbstractBinaryOperation {

    public CheckedMultiply(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    private void assertSafeOperation(int left, int right) {
        if (right > 0
                ? left > Integer.MAX_VALUE / right || left < Integer.MIN_VALUE / right
                : (right < -1
                ? left > Integer.MIN_VALUE / right || left < Integer.MAX_VALUE / right
                : right == -1 && left == Integer.MIN_VALUE)) {
            throw new ArithmeticException("[ERROR] Overflow: cannot to " +
                    "safely multiply " + left + "*" + right);
        }
    }

    @Override
    public int operate(int left, int right) {
        assertSafeOperation(left, right);
        return left * right;
    }
}
