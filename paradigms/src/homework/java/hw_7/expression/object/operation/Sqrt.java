/**
 * Nariman Safiulin (woofilee)
 * File: Sqrt.java
 * Created on: Apr 23, 2016
 */

package expression.object.operation;

import expression.ExpressionObject;
import expression.ExpressionNumber;
import expression.object.AbstractUnaryOperation;

public class Sqrt<T> extends AbstractUnaryOperation<T> {
    public Sqrt(ExpressionObject<T> object) {
        super(object);
    }

    @Override
    protected ExpressionNumber<T> operate(ExpressionNumber<T> num) {
        return num.sqrt();
    }
}
