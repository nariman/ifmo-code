/**
 * Nariman Safiulin (woofilee)
 * File: Add.java
 * Created on: Apr 23, 2016
 */

package expression.object.operation;

import expression.ExpressionNumber;
import expression.ExpressionObject;
import expression.object.AbstractBinaryOperation;

public class Add<T> extends AbstractBinaryOperation<T> {
    public Add(ExpressionObject<T> left, ExpressionObject<T> right) {
        super(left, right);
    }

    @Override
    protected ExpressionNumber<T> operate(ExpressionNumber<T> left, ExpressionNumber<T> right) {
        return left.add(right);
    }
}
