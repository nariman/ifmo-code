/**
 * Nariman Safiulin (woofilee)
 * File: ExpressionObject.java
 * Created on: Апр. 23, 2016
 */

package expression;

public interface ExpressionObject<T> {
    ExpressionNumber<T> evaluate(ExpressionNumber<T> x, ExpressionNumber<T> y, ExpressionNumber<T> z);
}
