/**
 * Nariman Safiulin (woofilee)
 * File: CheckedLogarithm.java
 * Created on: Apr 03, 2016
 */

package expression.exceptions;

import expression.AbstractBinaryOperation;
import expression.TripleExpression;

public class CheckedLogarithm extends AbstractBinaryOperation {
    public CheckedLogarithm(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    private void assertSafeOperation(int left, int right) {
        if (left <= 0 || right <= 0 || right == 1) {
            throw new ArithmeticException(
                    "[ERROR] Cannot to get logarithm due incorrect " +
                            "expression " + left + "//" + right);
        }
    }

    @Override
    protected int operate(int left, int right) {
        assertSafeOperation(left, right);

        return (int) (Math.log((double) left) / Math.log((double) right));

//        int answer = 0;
//        while (left >= right) {
//            left /= right;
//            ++answer;
//        }
//        return answer;
    }
}
