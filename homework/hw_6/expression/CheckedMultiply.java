/**
 * Nariman Safiulin (woofilee)
 * File: CheckedMultiply.java
 * Created on: Mar 27, 2016
 */

package expression;

public class CheckedMultiply extends CheckedAbstractBinaryOperation {

    public CheckedMultiply(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    private void assertOverflow(int left, int right) {
        if (right > 0 ? left > Integer.MAX_VALUE/right
                || left < Integer.MIN_VALUE/right
                : (right < -1 ? left > Integer.MIN_VALUE/right
                || left < Integer.MAX_VALUE/right
                : right == -1 && left == Integer.MIN_VALUE)) {
            throw new ArithmeticException("[ERROR] Overflow");
        }
    }

    @Override
    public int operate(int left, int right) {
        assertOverflow(left, right);
        return left * right;
    }
}
