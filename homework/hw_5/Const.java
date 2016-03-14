/**
 * Nariman Safiulin (woofilee)
 * File: Const.java
 * Created on: Mar 14, 2016
 */

public class Const implements ExpressionObject {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Const(int value) {
        this.setValue(value);
    }

    @Override
    public int evaluate(int variableValue) {
        return this.getValue();
    }
}
