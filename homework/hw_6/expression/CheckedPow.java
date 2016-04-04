/**
 * Nariman Safiulin (woofilee)
 * File: CheckedPow.java
 * Created on: Apr 03, 2016
 */

package expression;

public class CheckedPow extends CheckedAbstractBinaryOperation {

    public CheckedPow(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    @Override
    public String nameSelfOperation() {
        return "CheckedPow";
    }

    private void assertSafeOperation(int left, int right) {
        if (right < 0) {
            throw new ArithmeticException("[ERROR] Cannot to get pow due exponent number less than zero");
        }

        if (left == 0 && right == 0) {
            throw new ArithmeticException("[ERROR] Cannot to get pow due undefined answer for 0**0");
        }
    }

    @Override
    public int operate(int left, int right) {
        assertSafeOperation(left, right);
//        return (int) Math.pow((double) left, (double) right);
        if (left == 0) {
            return 0;
        }

        int res = 1;
        for (int i = 0; i < right; i++) {
            if (left > 0 ? res > Integer.MAX_VALUE / left
                    || res < Integer.MIN_VALUE / left
                    : (left < -1 ? res > Integer.MIN_VALUE / left
                    || res < Integer.MAX_VALUE / left
                    : left == -1 && res == Integer.MIN_VALUE)) {
                throw new ArithmeticException("[ERROR] Overflow: cannot to safely pow " + left + "**" + right);
            }

            res *= left;
        }
        return res;
    }
}
