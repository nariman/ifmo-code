/**
 * Nariman Safiulin (woofilee)
 * File: IntegerNumber.java
 * Created on: Apr 23, 2016
 */

package expression.number;

import expression.ExpressionNumber;

public class IntegerNumber extends AbstractNumber<Integer> {

    public IntegerNumber(Integer value) {
        super(value);
    }

    @Override
    public AbstractNumber<Integer> getInstance(Integer value) {
        return new IntegerNumber(value);
    }

    @Override
    public ExpressionNumber<Integer> abs() {
        return value < 0 ? negate() : new IntegerNumber(value);
    }

    @Override
    public ExpressionNumber<Integer> add(ExpressionNumber<Integer> num) {
        return new IntegerNumber(value + num.getValue());
    }

    @Override
    public ExpressionNumber<Integer> divide(ExpressionNumber<Integer> num) {
        return new IntegerNumber(value / num.getValue());
    }

    @Override
    public ExpressionNumber<Integer> log(ExpressionNumber<Integer> num) {
        if (value <= 0 || num.getValue() <= 0 || num.getValue() == 1) {
            throw new ArithmeticException(
                    "[ERROR] Cannot to get logarithm due incorrect " +
                            "expression " + value + "//" + num.getValue());
        }
        return new IntegerNumber((int) (Math.log((double) value) / Math.log((double) num.getValue())));

//        Integer left = value;
//        Integer right = num.getValue();
//        Integer res = 0;
//        while (left >= right) {
//            left /= right;
//            res++;
//        }
//        return new IntegerNumber(res);
    }

    @Override
    public ExpressionNumber<Integer> mod(ExpressionNumber<Integer> num) {
        return new IntegerNumber(value % num.getValue());
    }

    @Override
    public ExpressionNumber<Integer> negate() {
        return new IntegerNumber(-value);
    }

    @Override
    public ExpressionNumber<Integer> multiply(ExpressionNumber<Integer> num) {
        return new IntegerNumber(value * num.getValue());
    }

    @Override
    public ExpressionNumber<Integer> pow(ExpressionNumber<Integer> num) {
        int left = value;
        if (left == 0) {
            return new IntegerNumber(0);
        }

        int right = num.getValue();
        return new IntegerNumber((int) Math.pow((double) left, (double) right));

//        int res = 1;
//        while (right != 0) {
//            if (right % 2 != 0) {
//                res = res * left;
//                if (right == 1) {
//                    break;
//                }
//            }
//            left = res * left;
//            right /= 2;
//        }
//
//        return new IntegerNumber(res);
    }

    @Override
    public ExpressionNumber<Integer> sqrt() {
        if (value < 0) {
            throw new ArithmeticException("[ERROR] Cannot to get square " +
                    "root of negative value " + value);
        }
        return new IntegerNumber((int) Math.sqrt((double) value));
//        int num = value;
//
//        if (num == 0) {
//            return new IntegerNumber(0);
//        }
//
//        int div = num, res = num;
//
//        while (true) {
//            div = (num / div + div) >> 1;
//            if (res > div) {
//                res = div;
//            } else {
//                return new IntegerNumber(res);
//            }
//        }
    }

    @Override
    public ExpressionNumber<Integer> subtract(ExpressionNumber<Integer> num) {
        return new IntegerNumber(value - num.getValue());
    }
}
