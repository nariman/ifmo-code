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

    @Override
    public String nameSelfOperation() {
        return "CheckedMultiply";
    }

    private void assertSafeOperation(int left, int right) {
        if (right > 0 ? left > Integer.MAX_VALUE / right
                || left < Integer.MIN_VALUE / right
                : (right < -1 ? left > Integer.MIN_VALUE / right
                || left < Integer.MAX_VALUE / right
                : right == -1 && left == Integer.MIN_VALUE)) {
            throw new ArithmeticException("[ERROR] Overflow: cannot to safely multiply " + left + "*" + right);
        }
    }

    @Override
    public int operate(int left, int right) {
        assertSafeOperation(left, right);
        return left * right;
    }
}
