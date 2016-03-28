/**
 * Nariman Safiulin (woofilee)
 * File: CheckedDivide.java
 * Created on: Mar 27, 2016
 */

package expression;

public class CheckedDivide extends CheckedAbstractBinaryOperation {

    public CheckedDivide(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    private void assertOverflow(int left, int right) {
        if ((left == Integer.MIN_VALUE) && (right == -1)) {
            throw new ArithmeticException("[ERROR] Overflow");
        }
    }

    private void assertDBZ(int right) {
        if (right == 0) {
            throw new ArithmeticException("[ERROR] Division by zero");
        }
    }

    @Override
    public int operate(int left, int right) throws RuntimeException {
        assertOverflow(left, right);
        assertDBZ(right);
        return left / right;
    }
}
