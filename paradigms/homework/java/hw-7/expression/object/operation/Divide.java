/**
 * Nariman Safiulin (woofilee)
 * File: Divide.java
 * Created on: Apr 23, 2016
 */

package expression.object.operation;

import expression.ExpressionNumber;
import expression.ExpressionObject;
import expression.object.AbstractBinaryOperation;

public class Divide<T> extends AbstractBinaryOperation<T> {
    public Divide(ExpressionObject<T> left, ExpressionObject<T> right) {
        super(left, right);
    }

    @Override
    protected ExpressionNumber<T> operate(ExpressionNumber<T> left, ExpressionNumber<T> right) {
        return left.divide(right);
    }
}
