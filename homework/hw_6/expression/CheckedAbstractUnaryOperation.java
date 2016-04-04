/**
 * Nariman Safiulin (woofilee)
 * File: CheckedAbstractUnaryOperation.java
 * Created on: Mar 28, 2016
 */

package expression;

public abstract class CheckedAbstractUnaryOperation implements CheckedOperation {
    private final TripleExpression object;

    public CheckedAbstractUnaryOperation(TripleExpression object) {
        this.object = object;
    }

    public String nameSelf() {
        return "new " + this.nameSelfOperation() + "(" + object.nameSelf() + ")";
    }

    abstract protected int operate(int object);

    @Override
    public int evaluate(int x, int y, int z) {
        return operate(object.evaluate(x, y, z));
    }
}
