/**
 * Nariman Safiulin (woofilee)
 * File: Constant.java
 * Created on: Mar 27, 2016
 */

package expression;

public class Constant implements TripleExpression {
    private final int value;

    public Constant(int value) {
        this.value = value;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value;
    }
}
