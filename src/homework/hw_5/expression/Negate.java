/**
 * Nariman Safiulin (woofilee)
 * File: CheckedNegate.java
 * Created on: Mar 28, 2016
 */

package expression;

public class Negate extends AbstractUnaryOperation {

    public Negate(ExpressionObject object) {
        super(object);
    }

    @Override
    protected int operate(int object) {
        return -object;
    }

    @Override
    protected double operate(double object) {
        return -object;
    }
}
