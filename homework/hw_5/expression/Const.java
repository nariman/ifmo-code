/**
 * Nariman Safiulin (woofilee)
 * File: Const.java
 * Created on: Mar 27, 2016
 */

package expression;

public class Const implements ExpressionObject {
    private final Number value;
    private final Class type;

    public Const(int value) {
        this.value = value;
        type = int.class;
    }

    public Const(double value) {
        this.value = value;
        type = double.class;
    }

    @Override
    public int evaluate(int x) {
        if (type == int.class) {
            return (int) value;
        } else {
            return 0;
        }
    }

    @Override
    public double evaluate(double x) {
        if (type == double.class) {
            return (double) value;
        } else if (type == int.class) {
            return value.doubleValue();
        } else {
            return 0.0;
        }
    }

    @Override
    public int evaluate(int x, int y, int z) {
        if (type == int.class) {
            return (int) value;
        } else {
            return 0;
        }
    }
}
