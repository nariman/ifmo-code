/**
 * Nariman Safiulin (woofilee)
 * File: Constant.java
 * Created on: Apr 23, 2016
 */

package expression.object;

import expression.ExpressionNumber;
import expression.ExpressionObject;

public class Constant<T> implements ExpressionObject<T> {
    private ExpressionNumber<T> value;

    public Constant(ExpressionNumber<T> value) {
        this.value = value;
    }

    @Override
    public ExpressionNumber<T> evaluate(ExpressionNumber<T> x, ExpressionNumber<T> y, ExpressionNumber<T> z) {
        return value;
    }
}
