/**
 * Nariman Safiulin (woofilee)
 * File: Log.java
 * Created on: Apr 24, 2016
 */

package expression.object.operation;

import expression.ExpressionObject;
import expression.ExpressionNumber;
import expression.object.AbstractBinaryOperation;

public class Mod<T> extends AbstractBinaryOperation<T> {
    public Mod(ExpressionObject<T> left, ExpressionObject<T> right) {
        super(left, right);
    }

    @Override
    protected ExpressionNumber<T> operate(ExpressionNumber<T> left, ExpressionNumber<T> right) {
        return left.mod(right);
    }
}
