/**
 * Nariman Safiulin (woofilee)
 * File: DoubleNumber.java
 * Created on: Apr 23, 2016
 */

package expression.number;

import expression.ExpressionNumber;

public class DoubleNumber extends AbstractNumber<Double> {

    public DoubleNumber(Double value) {
        super(value);
    }

    @Override
    public AbstractNumber<Double> getInstance(Double value) {
        return new DoubleNumber(value);
    }

    @Override
    public ExpressionNumber<Double> abs() {
        return value < 0 ? negate() : new DoubleNumber(value);
    }

    @Override
    public ExpressionNumber<Double> add(ExpressionNumber<Double> num) {
        return new DoubleNumber(value + num.getValue());
    }

    @Override
    public ExpressionNumber<Double> divide(ExpressionNumber<Double> num) {
        return new DoubleNumber(value / num.getValue());
    }

    @Override
    public ExpressionNumber<Double> log(ExpressionNumber<Double> num) {
        if (value <= 0 || num.getValue() <= 0 || num.getValue() == 1) {
            throw new ArithmeticException(
                    "[ERROR] Cannot to get logarithm due incorrect " +
                            "expression " + value + "//" + num.getValue());
        }
        return new DoubleNumber(Math.log(value) / Math.log(num.getValue()));
    }

    @Override
    public ExpressionNumber<Double> mod(ExpressionNumber<Double> num) {
        return new DoubleNumber(value % num.getValue());
    }

    @Override
    public ExpressionNumber<Double> multiply(ExpressionNumber<Double> num) {
        return new DoubleNumber(value * num.getValue());
    }

    @Override
    public ExpressionNumber<Double> negate() {
        return new DoubleNumber(-value);
    }

    @Override
    public ExpressionNumber<Double> pow(ExpressionNumber<Double> num) {
        if (value == 0.0) {
            return new DoubleNumber(0.0);
        }

        return new DoubleNumber(Math.pow(value, num.getValue()));
    }

    @Override
    public ExpressionNumber<Double> sqrt() {
        if (value < 0) {
            throw new ArithmeticException("[ERROR] Cannot to get square " +
                    "root of negative value " + value);
        }
        return new DoubleNumber(Math.sqrt(value));
    }

    @Override
    public ExpressionNumber<Double> subtract(ExpressionNumber<Double> num) {
        return new DoubleNumber(value - num.getValue());
    }
}
