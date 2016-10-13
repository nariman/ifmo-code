/**
 * Nariman Safiulin (woofilee)
 * File: Log.java
 * Created on: Apr 23, 2016
 */

package expression.object.operation;

import expression.ExpressionObject;
import expression.ExpressionNumber;
import expression.object.AbstractBinaryOperation;

public class Log<T> extends AbstractBinaryOperation<T> {
    public Log(ExpressionObject<T> left, ExpressionObject<T> right) {
        super(left, right);
    }

    @Override
    protected ExpressionNumber<T> operate(ExpressionNumber<T> left, ExpressionNumber<T> right) {
        return left.log(right);
    }
}
