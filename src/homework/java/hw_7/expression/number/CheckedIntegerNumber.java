/**
 * Nariman Safiulin (woofilee)
 * File: CheckedIntegerNumber.java
 * Created on: Apr 24, 2016
 */

package expression.number;

import expression.ExpressionNumber;

public class CheckedIntegerNumber extends AbstractNumber<Integer> {
    public CheckedIntegerNumber(Integer value) {
        super(value);
    }

    @Override
    public AbstractNumber<Integer> getInstance(Integer value) {
        return new CheckedIntegerNumber(value);
    }

    @Override
    public ExpressionNumber<Integer> abs() {
        if (value == Integer.MIN_VALUE) {
            throw new ArithmeticException("[ERROR] Overflow: cannot to safely" +
                    " get absolute value of " + value);
        }
        return value < 0 ? negate() : new IntegerNumber(value);
    }

    @Override
    public ExpressionNumber<Integer> add(ExpressionNumber<Integer> num) {
        if (num.getValue() > 0
                ? value > Integer.MAX_VALUE - num.getValue()
                : value < Integer.MIN_VALUE - num.getValue()) {
            throw new ArithmeticException("[ERROR] Overflow: cannot to safely sum " + value + "+" + num.getValue());
        }
        return new CheckedIntegerNumber(value + num.getValue());
    }

    @Override
    public ExpressionNumber<Integer> divide(ExpressionNumber<Integer> num) {
        if ((value == Integer.MIN_VALUE) && (num.getValue() == -1)) {
            throw new ArithmeticException("[ERROR] Overflow: cannot to " +
                    "safely divide " + value + "/" + num.getValue());
        }

        if (num.getValue() == 0) {
            throw new ArithmeticException("[ERROR] Division by zero "+
                    value + "/" + num.getValue());
        }

        return new CheckedIntegerNumber(value / num.getValue());
    }

    @Override
    public ExpressionNumber<Integer> log(ExpressionNumber<Integer> num) {
        if (value <= 0 || num.getValue() <= 0 || num.getValue() == 1) {
            throw new ArithmeticException(
                    "[ERROR] Cannot to get logarithm due incorrect " +
                            "expression " + value + "//" + num.getValue());
        }
        return new CheckedIntegerNumber((int) (Math.log((double) value) / Math.log((double) num.getValue())));
    }

    @Override
    public ExpressionNumber<Integer> mod(ExpressionNumber<Integer> num) {
        return new CheckedIntegerNumber(value % num.getValue());
    }

    @Override
    public ExpressionNumber<Integer> negate() {
        if (value == Integer.MIN_VALUE) {
            throw new ArithmeticException("[ERROR] Overflow: cannot to " +
                    "safely negate value of " + value);
        }
        return new CheckedIntegerNumber(-value);
    }

    @Override
    public ExpressionNumber<Integer> multiply(ExpressionNumber<Integer> num) {
        if (num.getValue() > 0
                ? value > Integer.MAX_VALUE / num.getValue() || value < Integer.MIN_VALUE / num.getValue()
                : (num.getValue() < -1
                ? value > Integer.MIN_VALUE / num.getValue() || value < Integer.MAX_VALUE / num.getValue()
                : num.getValue() == -1 && value == Integer.MIN_VALUE)) {
            throw new ArithmeticException("[ERROR] Overflow: cannot to " +
                    "safely multiply " + value + "*" + num.getValue());
        }
        return new CheckedIntegerNumber(value * num.getValue());
    }

    @Override
    public ExpressionNumber<Integer> pow(ExpressionNumber<Integer> num) {
        int left = value;
        if (left == 0) {
            return new CheckedIntegerNumber(0);
        }

        int right = num.getValue();
        int res = 1;
        while (right != 0) {
            if (right % 2 != 0) {
                res = checkedPowsMultiply(res, left);
                if (right == 1) {
                    break;
                }
            }
            left = checkedPowsMultiply(left, left);
            right /= 2;
        }

        return new CheckedIntegerNumber(res);
    }

    @Override
    public ExpressionNumber<Integer> sqrt() {
        if (value < 0) {
            throw new ArithmeticException("[ERROR] Cannot to get square " +
                    "root of negative value " + value);
        }
        return new CheckedIntegerNumber((int) Math.sqrt((double) value));
    }

    @Override
    public ExpressionNumber<Integer> subtract(ExpressionNumber<Integer> num) {
        if (num.getValue() > 0
                ? value < Integer.MIN_VALUE + num.getValue()
                : value > Integer.MAX_VALUE + num.getValue()) {
            throw new ArithmeticException("[ERROR] Overflow: cannot to " +
                    "safely subtract " + value + "-" + num.getValue());
        }
        return new CheckedIntegerNumber(value - num.getValue());
    }

    private int checkedPowsMultiply(int left, int right) {
        if (right > 0
                ? left > Integer.MAX_VALUE / right || left < Integer.MIN_VALUE / right
                : (right < -1
                ? left > Integer.MIN_VALUE / right || left < Integer.MAX_VALUE / right
                : right == -1 && left == Integer.MIN_VALUE)) {
            throw new ArithmeticException("[ERROR] Overflow: cannot to " +
                    "safely pow " + left + "*" + right);
        }
        return left * right;
    }
}
