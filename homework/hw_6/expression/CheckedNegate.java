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

    private void assertOverflow(int object) {
        if (object == Integer.MIN_VALUE) {
            throw new ArithmeticException("[ERROR] Overflow");
        }
    }

    @Override
    protected int operate(int object) {
        assertOverflow(object);
        return -object;
    }
}
