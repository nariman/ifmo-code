/**
 * Nariman Safiulin (woofilee)
 * File: CheckedNegate.java
 * Created on: Mar 28, 2016
 */

package expression;

public class CheckedNegate extends CheckedAbstractUnaryOperation {

    public CheckedNegate(TripleExpression object) {
        super(object);
    }

    @Override
    public String nameSelfOperation() {
        return "CheckedNegate";
    }

    private void assertSafeOperation(int value) {
        if (value == Integer.MIN_VALUE) {
            throw new ArithmeticException("[ERROR] Overflow: cannot to safely negate value of " + value);
        }
    }

    @Override
    protected int operate(int value) {
        assertSafeOperation(value);
        return -value;
    }
}
