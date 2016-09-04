/**
 * Nariman Safiulin (woofilee)
 * File: FloatNumber.java
 * Created on: Apr 24, 2016
 */

package expression.number;

import expression.ExpressionNumber;

public class FloatNumber extends AbstractNumber<Float> {
    public FloatNumber(Float value) {
        super(value);
    }

    @Override
    public AbstractNumber<Float> getInstance(Float value) {
        return new FloatNumber(value);
    }

    @Override
    public ExpressionNumber<Float> abs() {
        return value < 0 ? negate() : new FloatNumber(value);
    }

    @Override
    public ExpressionNumber<Float> add(ExpressionNumber<Float> num) {
        return new FloatNumber(value + num.getValue());
    }

    @Override
    public ExpressionNumber<Float> divide(ExpressionNumber<Float> num) {
        return new FloatNumber(value / num.getValue());
    }

    @Override
    public ExpressionNumber<Float> log(ExpressionNumber<Float> num) {
        return new FloatNumber((float) (Math.log(value) / Math.log(num.getValue())));
    }

    @Override
    public ExpressionNumber<Float> mod(ExpressionNumber<Float> num) {
        return new FloatNumber(value % num.getValue());
    }

    @Override
    public ExpressionNumber<Float> multiply(ExpressionNumber<Float> num) {
        return new FloatNumber(value * num.getValue());
    }

    @Override
    public ExpressionNumber<Float> negate() {
        return new FloatNumber(-value);
    }

    @Override
    public ExpressionNumber<Float> pow(ExpressionNumber<Float> num) {
        return new FloatNumber((float) Math.pow(value, num.getValue()));
    }

    @Override
    public ExpressionNumber<Float> sqrt() {
        return new FloatNumber((float) Math.sqrt(value));
    }

    @Override
    public ExpressionNumber<Float> subtract(ExpressionNumber<Float> num) {
        return new FloatNumber(value - num.getValue());
    }
}
