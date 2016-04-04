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

    @Override
    public String nameSelfOperation() {
        return "CheckedSubtract";
    }

    private void assertSafeOperation(int left, int right) {
        if (right > 0 ? left < Integer.MIN_VALUE + right : left > Integer.MAX_VALUE + right) {
            throw new ArithmeticException("[ERROR] Overflow: cannot to safely subtract " + left + "-" + right);
        }
    }

    @Override
    public int operate(int left, int right) {
        assertSafeOperation(left, right);
        return left - right;
    }
}
