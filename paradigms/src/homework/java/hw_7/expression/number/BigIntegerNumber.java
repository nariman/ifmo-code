/**
 * Nariman Safiulin (woofilee)
 * File: BigIntegerNumber.java
 * Created on: Apr 24, 2016
 */

package expression.number;

import expression.ExpressionNumber;

import java.math.BigInteger;

public class BigIntegerNumber extends AbstractNumber<BigInteger> {
    public BigIntegerNumber(BigInteger value) {
        super(value);
    }

    @Override
    public AbstractNumber<BigInteger> getInstance(BigInteger value) {
        return new BigIntegerNumber(value);
    }

    @Override
    public ExpressionNumber<BigInteger> abs() {
        return value.compareTo(new BigInteger("0")) < 0 ? negate() : new BigIntegerNumber(value);
    }

    @Override
    public ExpressionNumber<BigInteger> add(ExpressionNumber<BigInteger> num) {
        return new BigIntegerNumber(value.add(num.getValue()));
    }

    @Override
    public ExpressionNumber<BigInteger> divide(ExpressionNumber<BigInteger> num) {
        return new BigIntegerNumber(value.divide(num.getValue()));
    }

    @Override
    public ExpressionNumber<BigInteger> log(ExpressionNumber<BigInteger> num) {
        if (value.compareTo(new BigInteger("0")) <= 0
                || num.getValue().compareTo(new BigInteger("0")) <= 0
                || num.getValue().compareTo(new BigInteger("1")) == 1) {
            throw new ArithmeticException(
                    "[ERROR] Cannot to get logarithm due incorrect " +
                            "expression " + value + "//" + num.getValue());
        }

        BigInteger left = value;
        BigInteger right = num.getValue();
        BigInteger res = new BigInteger("0");
        while (left.compareTo(right) >= 0) {
            left = left.divide(right);
            res = res.add(new BigInteger("1"));
        }
        return new BigIntegerNumber(res);
    }

    @Override
    public ExpressionNumber<BigInteger> mod(ExpressionNumber<BigInteger> num) {
        return new BigIntegerNumber(value.mod(num.getValue()));
    }

    @Override
    public ExpressionNumber<BigInteger> multiply(ExpressionNumber<BigInteger> num) {
        return new BigIntegerNumber(value.multiply(num.getValue()));
    }

    @Override
    public ExpressionNumber<BigInteger> negate() {
        return new BigIntegerNumber(value.negate());
    }

    @Override
    public ExpressionNumber<BigInteger> pow(ExpressionNumber<BigInteger> num) {
        return new BigIntegerNumber(value.pow(Integer.parseInt(num.getValue().toString())));
    }

    @Override
    public ExpressionNumber<BigInteger> sqrt() {
        if (value.compareTo(new BigInteger("0")) <= 0) {
            throw new ArithmeticException("[ERROR] Cannot to get square " +
                    "root of negative value " + value);
        }
        if (value.compareTo(new BigInteger("0")) == 0) {
            return new BigIntegerNumber(new BigInteger("0"));
        }

        BigInteger num = value;
        BigInteger div = num, res = num;

        while (true) {
            div = num.divide(div).add(div).shiftRight(1);
            if (res.compareTo(div) > 0) {
                res = div;
            } else {
                return new BigIntegerNumber(res);
            }
        }
    }

    @Override
    public ExpressionNumber<BigInteger> subtract(ExpressionNumber<BigInteger> num) {
        return new BigIntegerNumber(value.subtract(num.getValue()));
    }
}
