/**
 * Nariman Safiulin (woofilee)
 * File: CheckedVariable.java
 * Created on: Mar 27, 2016
 */

package expression;

public class CheckedVariable implements TripleExpression {
    private final String name;

    public CheckedVariable(String name) {
        this.name = name;
    }

    @Override
    public String nameSelf() {
        return "new CheckedVariable(\"" + name + "\")";
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
