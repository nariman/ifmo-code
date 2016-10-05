/**
 * Nariman Safiulin (woofilee)
 * File: ExpressionNumber.java
 * Created on: Apr 23, 2016
 */

package expression;

public interface ExpressionNumber<T> extends Comparable<ExpressionNumber<T>> {
    T getValue();

    ExpressionNumber<T> abs();

    ExpressionNumber<T> add(ExpressionNumber<T> num);

    ExpressionNumber<T> divide(ExpressionNumber<T> num);

    ExpressionNumber<T> log(ExpressionNumber<T> num);

    ExpressionNumber<T> mod(ExpressionNumber<T> num);

    ExpressionNumber<T> multiply(ExpressionNumber<T> num);

    ExpressionNumber<T> negate();

    ExpressionNumber<T> pow(ExpressionNumber<T> num);

    ExpressionNumber<T> sqrt();

    ExpressionNumber<T> square();

    ExpressionNumber<T> subtract(ExpressionNumber<T> num);
}
