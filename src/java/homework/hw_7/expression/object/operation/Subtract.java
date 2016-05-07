/**
 * Nariman Safiulin (woofilee)
 * File: Subtract.java
 * Created on: Apr 23, 2016
 */

package expression.object.operation;

import expression.ExpressionObject;
import expression.ExpressionNumber;
import expression.object.AbstractBinaryOperation;

public class Subtract<T> extends AbstractBinaryOperation<T> {
    public Subtract(ExpressionObject<T> left, ExpressionObject<T> right) {
        super(left, right);
    }

    @Override
    protected ExpressionNumber<T> operate(ExpressionNumber<T> left, ExpressionNumber<T> right) {
        return left.subtract(right);
    }
}
