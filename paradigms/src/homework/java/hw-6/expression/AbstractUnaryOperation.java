/**
 * Nariman Safiulin (woofilee)
 * File: AbstractUnaryOperation.java
 * Created on: Mar 28, 2016
 */

package expression;

public abstract class AbstractUnaryOperation implements Operation {
    private final TripleExpression object;

    public AbstractUnaryOperation(TripleExpression object) {
        this.object = object;
    }

    abstract protected int operate(int object);

    @Override
    public int evaluate(int x, int y, int z) {
        return operate(object.evaluate(x, y, z));
    }
}
