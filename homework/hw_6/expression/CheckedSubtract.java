/**
 * Nariman Safiulin (woofilee)
 * File: CheckedSubtract.java
 * Created on: Mar 27, 2016
 */

package expression;

public class CheckedSubtract extends CheckedAbstractBinaryOperation {

    public CheckedSubtract(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    private void assertOverflow(int left, int right) {
        if (right > 0 ? left < Integer.MIN_VALUE + right : left > Integer.MAX_VALUE + right) {
            throw new ArithmeticException("[ERROR] Overflow");
        }
    }

    @Override
    public int operate(int left, int right) {
        assertOverflow(left, right);
        return left - right;
    }
}
