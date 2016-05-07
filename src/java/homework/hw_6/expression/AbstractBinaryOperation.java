/**
 * Nariman Safiulin (woofilee)
 * File: AbstractBinaryOperation.java
 * Created on: Mar 24, 2016
 */

package expression;

public abstract class AbstractBinaryOperation implements Operation {
    private final TripleExpression left;
    private final TripleExpression right;

    public AbstractBinaryOperation(TripleExpression left, TripleExpression right) {
        this.left = left;
        this.right = right;
    }

    abstract protected int operate(int left, int right);

    @Override
    public int evaluate(int x, int y, int z) {
        return operate(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }
}
