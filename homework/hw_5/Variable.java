/**
 * Nariman Safiulin (woofilee)
 * File: Variable.java
 * Created on: Mar 14, 2016
 */

public class Variable implements ExpressionObject {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Variable(String name) {
        this.setName(name);
    }

    @Override
    public int evaluate(int variableValue) {
        return variableValue;
    }
}
