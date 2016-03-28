/**
 * Nariman Safiulin (woofilee)
 * File: CheckedMultiply.java
 * Created on: Mar 27, 2016
 */

package expression;

public class Multiply extends AbstractBinaryOperation {

    public Multiply(ExpressionObject left, ExpressionObject right) {
        super(left, right);
    }

    @Override
    public int operate(int left, int right) {
        return left * right;
    }

    @Override
    public double operate(double left, double right) {
        return left * right;
    }
}
