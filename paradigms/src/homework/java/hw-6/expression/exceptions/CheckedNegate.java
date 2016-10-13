/**
 * Nariman Safiulin (woofilee)
 * File: CheckedNegate.java
 * Created on: Mar 28, 2016
 */

package expression.exceptions;

import expression.AbstractUnaryOperation;
import expression.TripleExpression;

public class CheckedNegate extends AbstractUnaryOperation {

    public CheckedNegate(TripleExpression object) {
        super(object);
    }

    private void assertSafeOperation(int value) {
        if (value == Integer.MIN_VALUE) {
            throw new ArithmeticException("[ERROR] Overflow: cannot to " +
                    "safely negate value of " + value);
        }
    }

    @Override
    protected int operate(int value) {
        assertSafeOperation(value);
        return -value;
    }
}
