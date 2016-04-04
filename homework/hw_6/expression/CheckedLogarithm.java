/**
 * Nariman Safiulin (woofilee)
 * File: CheckedLogarithm.java
 * Created on: Apr 03, 2016
 */

package expression;

public class CheckedLogarithm extends CheckedAbstractBinaryOperation {
    public CheckedLogarithm(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    @Override
    public String nameSelfOperation() {
        return "CheckedLogarithm";
    }

    private void assertSafeOperation(int left, int right) {
        if (left <= 0 || right <= 0 || right == 1) {
            throw new ArithmeticException(
                    "[ERROR] Cannot to get logarithm due incorrect expression " + left + "//" + right);
        }
    }

    @Override
    protected int operate(int left, int right) {
        assertSafeOperation(left, right);
        return (int) ((Math.log(left) / Math.log(right)));
    }
}
