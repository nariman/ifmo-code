/**
 * Nariman Safiulin (woofilee)
 * File: Variable.java
 * Created on: Март 27, 2016
 */

package expression;

public class Variable implements ExpressionObject {
    private final String name;

    Variable(String name) {
        this.name = name;
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public double evaluate(double x) {
        return x;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        switch (name) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
            default:
                return 0;
        }
    }
}
