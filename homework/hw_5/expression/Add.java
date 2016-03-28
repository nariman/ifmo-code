/**
 * Nariman Safiulin (woofilee)
 * File: CheckedAdd.java
 * Created on: Mar 27, 2016
 */

package expression;

public class Add extends AbstractBinaryOperation {

    public Add(ExpressionObject first, ExpressionObject second) {
        super(first, second);
    }

    @Override
    public int operate(int left, int right) {
        return left + right;
    }

    @Override
    public double operate(double left, double right) {
        return left + right;
    }
}
