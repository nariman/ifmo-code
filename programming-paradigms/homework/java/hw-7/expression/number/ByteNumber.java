/**
 * Nariman Safiulin (woofilee)
 * File: ByteNumber.java
 * Created on: Apr 24, 2016
 */

package expression.number;

import expression.ExpressionNumber;

public class ByteNumber extends AbstractNumber<Byte> {
    public ByteNumber(Byte value) {
        super(value);
    }

    @Override
    public AbstractNumber<Byte> getInstance(Byte value) {
        return new ByteNumber(value);
    }

    @Override
    public ExpressionNumber<Byte> abs() {
        return value < 0 ? negate() : new ByteNumber(value);
    }

    @Override
    public ExpressionNumber<Byte> add(ExpressionNumber<Byte> num) {
        return new ByteNumber((byte) (value + num.getValue()));
    }

    @Override
    public ExpressionNumber<Byte> divide(ExpressionNumber<Byte> num) {
        return new ByteNumber((byte) (value / num.getValue()));
    }

    @Override
    public ExpressionNumber<Byte> log(ExpressionNumber<Byte> num) {
        return new ByteNumber((byte) (Math.log((double) value) / Math.log((double) num.getValue())));
    }

    @Override
    public ExpressionNumber<Byte> mod(ExpressionNumber<Byte> num) {
        return new ByteNumber((byte) (value % num.getValue()));
    }

    @Override
    public ExpressionNumber<Byte> multiply(ExpressionNumber<Byte> num) {
        return new ByteNumber((byte) (value * num.getValue()));
    }

    @Override
    public ExpressionNumber<Byte> negate() {
        return new ByteNumber((byte) -value);
    }

    @Override
    public ExpressionNumber<Byte> pow(ExpressionNumber<Byte> num) {
        return new ByteNumber((byte) Math.pow((double) value, (double) num.getValue()));
    }

    @Override
    public ExpressionNumber<Byte> sqrt() {
        return new ByteNumber((byte) Math.sqrt((double) value));
    }

    @Override
    public ExpressionNumber<Byte> subtract(ExpressionNumber<Byte> num) {
        return new ByteNumber((byte) (value - num.getValue()));
    }
}
