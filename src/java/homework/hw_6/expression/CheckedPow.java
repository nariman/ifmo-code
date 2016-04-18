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

    private int log(int left, int right) {
        int answer = 0;
        while (left >= right) {
            left /= right;
            ++answer;
        }
        return answer;
    }

    private void assertSafeOperation(int left, int right) {
        if (right < 0) {
            throw new ArithmeticException("[ERROR] Cannot to get pow due " +
                    "exponent number less than zero");
        }

        if (left == 0 && right == 0) {
            throw new ArithmeticException("[ERROR] Cannot to get pow due " +
                    "undefined answer for 0**0");
        }

        if (right * Math.log10(left) >= Math.log10(Integer.MAX_VALUE)) {
            throw new ArithmeticException("[ERROR] Overflow: cannot to " +
                    "safely multiply " + left + "*" + right);
        }
    }

    private int checkedMultiply(int left, int right) {
        if (right > 0
                ? left > Integer.MAX_VALUE / right || left < Integer.MIN_VALUE / right
                : (right < -1
                ? left > Integer.MIN_VALUE / right || left < Integer.MAX_VALUE / right
                : right == -1 && left == Integer.MIN_VALUE)) {
            throw new ArithmeticException("[ERROR] Overflow: cannot to " +
                    "safely multiply " + left + "*" + right);
        }
        return left * right;
    }

    @Override
    public int operate(int left, int right) {
        assertSafeOperation(left, right);
        if (left == 0) {
            return 0;
        }
//        return (int) Math.pow((double) left, (double) right);

        int res = 1;
        while (right != 0) {
            if (right % 2 == 1) {
                res = res * left;
                if (right == 1) {
                    break;
                }
            }
            right /= 2;
            left = left * left;
        }

        return res;
    }
}
