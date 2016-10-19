/**
 * Nariman Safiulin (woofilee)
 * File: AbstractUnaryOperation.java
 * Created on: Mar 28, 2016
 */

package expression;

public abstract class AbstractUnaryOperation implements Operation {
    private final ExpressionObject object;

    public AbstractUnaryOperation(ExpressionObject object) {
        this.object = object;
    }

    abstract protected int operate(int object);
    abstract protected double operate(double object);

    @Override
    public int evaluate(int x) {
        return operate(object.evaluate(x));
    }

    @Override
    public double evaluate(double x) {
        return operate(object.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return operate(object.evaluate(x, y, z));
    }
}
