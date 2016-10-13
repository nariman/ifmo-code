/**
 * Nariman Safiulin (woofilee)
 * File: Variable.java
 * Created on: Apr 23, 2016
 */

package expression.object;

import expression.ExpressionNumber;
import expression.ExpressionObject;

public class Variable<T> implements ExpressionObject<T> {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public ExpressionNumber<T> evaluate(ExpressionNumber<T> x, ExpressionNumber<T> y, ExpressionNumber<T> z) {
        switch (name) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
            default:
                return null;
        }
    }
}
