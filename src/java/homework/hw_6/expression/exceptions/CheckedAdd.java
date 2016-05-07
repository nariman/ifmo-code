/**
 * Nariman Safiulin (woofilee)
 * File: CheckedAdd.java
 * Created on: Mar 27, 2016
 */

package expression.exceptions;

import expression.AbstractBinaryOperation;
import expression.TripleExpression;

public class CheckedAdd extends AbstractBinaryOperation {

    public CheckedAdd(TripleExpression first, TripleExpression second) {
        super(first, second);
    }

    private void assertSafeOperation(int left, int right) {
        if (right > 0
                ? left > Integer.MAX_VALUE - right
                : left < Integer.MIN_VALUE - right) {
            throw new ArithmeticException("[ERROR] Overflow");
        }
    }

    @Override
    public int operate(int left, int right) {
        assertSafeOperation(left, right);
        return left + right;
    }
}
