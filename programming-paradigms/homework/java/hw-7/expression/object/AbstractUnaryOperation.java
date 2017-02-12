/**
 * Nariman Safiulin (woofilee)
 * File: AbstractUnaryOperation.java
 * Created on: Apr 23, 2016
 */

package expression.object;

import expression.ExpressionNumber;
import expression.ExpressionObject;

public abstract class AbstractUnaryOperation<T> implements Operation<T> {
    private final ExpressionObject<T> object;

    public AbstractUnaryOperation(ExpressionObject<T> object) {
        this.object = object;
    }

    abstract protected ExpressionNumber<T> operate(ExpressionNumber<T> num);

    @Override
    public ExpressionNumber<T> evaluate(ExpressionNumber<T> x, ExpressionNumber<T> y, ExpressionNumber<T> z) {
        return operate(object.evaluate(x, y, z));
    }
}
