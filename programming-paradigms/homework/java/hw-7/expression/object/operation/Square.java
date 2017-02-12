/**
 * Nariman Safiulin (woofilee)
 * File: Square.java
 * Created on: Apr 24, 2016
 */

package expression.object.operation;

import expression.ExpressionNumber;
import expression.ExpressionObject;
import expression.object.AbstractUnaryOperation;

public class Square<T> extends AbstractUnaryOperation<T> {
    public Square(ExpressionObject<T> object) {
        super(object);
    }

    @Override
    protected ExpressionNumber<T> operate(ExpressionNumber<T> num) {
        return num.square();
    }
}
