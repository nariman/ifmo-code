/**
 * Nariman Safiulin (woofilee)
 * File: Negate.java
 * Created on: Apr 23, 2016
 */

package expression.object.operation;

import expression.ExpressionNumber;
import expression.ExpressionObject;
import expression.object.AbstractUnaryOperation;

public class Negate<T> extends AbstractUnaryOperation<T> {
    public Negate(ExpressionObject<T> object) {
        super(object);
    }

    @Override
    protected ExpressionNumber<T> operate(ExpressionNumber<T> num) {
        return num.negate();
    }
}
