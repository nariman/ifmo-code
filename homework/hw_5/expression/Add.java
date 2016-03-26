/**
 * Nariman Safiulin (woofilee)
 * File: Add.java
 * Created on: Mar 27, 2016
 */

package expression;

public class Add extends AbstractOperation {

    Add(ExpressionObject first, ExpressionObject second) {
        super(first, second);
    }

    public int operate(int left, int right) {
        return left + right;
    }

    public double operate(double left, double right) {
        return left + right;
    }
}
