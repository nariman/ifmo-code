/**
 * Nariman Safiulin (woofilee)
 * File: GenericTabulator.java
 * Created on: Apr 23, 2016
 */

package expression.generic;

import expression.*;
import expression.number.*;
import expression.parser.*;

import java.math.BigInteger;
import java.util.function.Function;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        switch (mode) {
            case "i":
                return evaluate(new Parser<>(new Tokenizer<Integer>() {
                    @Override
                    protected ExpressionNumber<Integer> parseConstToken(String num) {
                        return new CheckedIntegerNumber(Integer.parseInt(num));
                    }
                }).parse(expression), x1, x2, y1, y2, z1, z2, (num) -> new CheckedIntegerNumber(num));
            case "d":
                return evaluate(new Parser<>(new Tokenizer<Double>() {
                    @Override
                    protected ExpressionNumber<Double> parseConstToken(String num) {
                        return new DoubleNumber(Double.parseDouble(num));
                    }
                }).parse(expression), x1, x2, y1, y2, z1, z2, (num) -> new DoubleNumber((double) num));
            case "bi":
                return evaluate(new Parser<>(new Tokenizer<BigInteger>() {
                    @Override
                    protected ExpressionNumber<BigInteger> parseConstToken(String num) {
                        return new BigIntegerNumber(new BigInteger(num));
                    }
                }).parse(expression), x1, x2, y1, y2, z1, z2, (num) -> new BigIntegerNumber(new BigInteger(num.toString())));
            case "u":
                return evaluate(new Parser<>(new Tokenizer<Integer>() {
                    @Override
                    protected ExpressionNumber<Integer> parseConstToken(String num) {
                        return new IntegerNumber(Integer.parseInt(num));
                    }
                }).parse(expression), x1, x2, y1, y2, z1, z2, (num) -> new IntegerNumber(num));
            case "b":
                return evaluate(new Parser<>(new Tokenizer<Byte>() {
                    @Override
                    protected ExpressionNumber<Byte> parseConstToken(String num) {
                        return new ByteNumber(Byte.parseByte(num));
                    }
                }).parse(expression), x1, x2, y1, y2, z1, z2, (num) -> new ByteNumber(new Byte(num.toString())));
            case "f":
                return evaluate(new Parser<>(new Tokenizer<Float>() {
                    @Override
                    protected ExpressionNumber<Float> parseConstToken(String num) {
                        return new FloatNumber(Float.parseFloat(num));
                    }
                }).parse(expression), x1, x2, y1, y2, z1, z2, (num) -> new FloatNumber(new Float(num.toString())));
            default:
                throw new IllegalArgumentException("Arithmetic Mode must be one " +
                        "of 'i' for Integer with overflow checks, 'd' for Double, " +
                        "'bi' for BigInteger 'u' for Integer, 'b' for Byte or 'f' for Float");
        }
    }

    public <T> Object[][][] evaluate(ExpressionObject<T> expression, int x1, int x2, int y1, int y2, int z1, int z2, Function<Integer, ExpressionNumber<T>> f) {
        Object[][][] ans = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k <= z2; k++) {
                    try {
                        ans[i - x1][j - y1][k - z1] = expression.evaluate(f.apply(i), f.apply(j), f.apply(k)).getValue();
                    } catch (ArithmeticException | IllegalArgumentException e) {
                        ans[i - x1][j - y1][k - z1] = null;
                    }
                }
            }
        }
        return ans;
    }
}
