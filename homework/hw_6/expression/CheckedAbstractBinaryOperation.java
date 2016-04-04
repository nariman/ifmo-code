/**
 * Nariman Safiulin (woofilee)
 * File: CheckedAbstractBinaryOperation.java
 * Created on: Mar 24, 2016
 */

package expression;

public abstract class CheckedAbstractBinaryOperation implements CheckedOperation {
    private final TripleExpression left;
    private final TripleExpression right;

    public CheckedAbstractBinaryOperation(TripleExpression left, TripleExpression right) {
        this.left = left;
        this.right = right;
    }

    public String nameSelf() {
        return "new " + this.nameSelfOperation() + "(" + left.nameSelf() + ", " + right.nameSelf() + ")";
    }

    abstract protected int operate(int left, int right);

    @Override
    public int evaluate(int x, int y, int z) {
        return operate(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }
}
