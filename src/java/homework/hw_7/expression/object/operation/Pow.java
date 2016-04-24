/**
 * Nariman Safiulin (woofilee)
 * File: Pow.java
 * Created on: Apr 23, 2016
 */

package expression.object.operation;

import expression.ExpressionNumber;
import expression.ExpressionObject;
import expression.object.AbstractBinaryOperation;

public class Pow<T> extends AbstractBinaryOperation<T> {
    public Pow(ExpressionObject<T> left, ExpressionObject<T> right) {
        super(left, right);
    }

    @Override
    protected ExpressionNumber<T> operate(ExpressionNumber<T> left, ExpressionNumber<T> right) {
        return left.pow(right);
    }
}
