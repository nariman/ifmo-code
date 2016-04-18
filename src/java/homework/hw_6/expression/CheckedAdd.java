/**
 * Nariman Safiulin (woofilee)
 * File: CheckedAdd.java
 * Created on: Mar 27, 2016
 */

package expression;

public class CheckedAdd extends CheckedAbstractBinaryOperation {

    public CheckedAdd(TripleExpression first, TripleExpression second) {
        super(first, second);
    }

    @Override
    public String nameSelfOperation() {
        return "CheckedAdd";
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
