/**
 * Nariman Safiulin (woofilee)
 * File: AbstractBinaryOperation.java
 * Created on: Apr 23, 2016
 */

package expression.object;

import expression.ExpressionNumber;
import expression.ExpressionObject;

public abstract class AbstractBinaryOperation<T> implements Operation<T> {
    private final ExpressionObject<T> left;
    private final ExpressionObject<T> right;

    public AbstractBinaryOperation(ExpressionObject<T> left, ExpressionObject<T> right) {
        this.left = left;
        this.right = right;
    }

    abstract protected ExpressionNumber<T> operate(ExpressionNumber<T> left, ExpressionNumber<T> right);

    @Override
    public final ExpressionNumber<T> evaluate(ExpressionNumber<T> x, ExpressionNumber<T> y, ExpressionNumber<T> z) {
        return operate(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }
}
