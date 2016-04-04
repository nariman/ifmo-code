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

    @Override
    public String nameSelfOperation() {
        return "CheckedDivide";
    }

    private void assertSafeOperation(int left, int right) {
        if ((left == Integer.MIN_VALUE) && (right == -1)) {
            throw new ArithmeticException("[ERROR] Overflow: cannot to safely divide " + left + "/" + right);
        }

        if (right == 0) {
            throw new ArithmeticException("[ERROR] Division by zero " + left + "/" + right);
        }
    }

    @Override
    public int operate(int left, int right) throws RuntimeException {
        assertSafeOperation(left, right);
        return left / right;
    }
}
