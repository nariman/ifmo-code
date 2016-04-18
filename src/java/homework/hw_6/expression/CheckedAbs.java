/**
 * Nariman Safiulin (woofilee)
 * File: CheckedAbs.java
 * Created on: Mar 28, 2016
 */

package expression;

public class CheckedAbs extends CheckedAbstractUnaryOperation {

    public CheckedAbs(TripleExpression object) {
        super(object);
    }

    @Override
    public String nameSelfOperation() {
        return "CheckedAbs";
    }

    private void assertSafeOperation(int value) {
        if (value == Integer.MIN_VALUE) {
            throw new ArithmeticException("[ERROR] Overflow: cannot to safely" +
                    " get absolute value of " + value);
        }
    }

    @Override
    protected int operate(int value) {
        assertSafeOperation(value);
        return (value < 0) ? -value : value;
    }
}
