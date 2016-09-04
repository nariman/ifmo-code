/**
 * Nariman Safiulin (woofilee)
 * File: AbstractNumber.java
 * Created on: Apr 23, 2016
 */

package expression.number;

import expression.ExpressionNumber;

public abstract class AbstractNumber<T extends Comparable<T>> implements ExpressionNumber<T> {
    protected T value;

    public AbstractNumber(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    public abstract AbstractNumber<T> getInstance(T value);

    @Override
    public String toString() {
        return "AbstractNumber{" +
                "value=" + value +
                '}';
    }

    public ExpressionNumber<T> square() {
        return multiply(getInstance(value));
    }

    @Override
    public int compareTo(ExpressionNumber<T> o) {
        return value.compareTo(o.getValue());
    }
}
