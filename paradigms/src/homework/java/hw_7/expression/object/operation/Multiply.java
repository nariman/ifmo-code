/**
 * Nariman Safiulin (woofilee)
 * File: Multiply.java
 * Created on: Apr 23, 2016
 */

package expression.object.operation;

import expression.ExpressionObject;
import expression.ExpressionNumber;
import expression.object.AbstractBinaryOperation;

public class Multiply<T> extends AbstractBinaryOperation<T> {
    public Multiply(ExpressionObject<T> left, ExpressionObject<T> right) {
        super(left, right);
    }

    @Override
    protected ExpressionNumber<T> operate(ExpressionNumber<T> left, ExpressionNumber<T> right) {
        return left.multiply(right);
    }
}
