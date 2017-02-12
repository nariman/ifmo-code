/**
 * Nariman Safiulin (woofilee)
 * File: Subtract.java
 * Created on: Mar 27, 2016
 */

package expression;

public class Subtract extends AbstractBinaryOperation {

    public Subtract(ExpressionObject left, ExpressionObject right) {
        super(left, right);
    }

    @Override
    public int operate(int left, int right) {
        return left - right;
    }

    @Override
    public double operate(double left, double right) {
        return left - right;
    }
}
