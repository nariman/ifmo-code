/**
 * Nariman Safiulin (woofilee)
 * File: Operation.java
 * Created on: Mar 14, 2016
 */

public abstract class Operation implements ExpressionObject {
    protected ExpressionObject leftOperand;
    protected ExpressionObject rightOperand;

    Operation(ExpressionObject leftOperand, ExpressionObject rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public ExpressionObject getLeftOperand() {
        return leftOperand;
    }

    public ExpressionObject getRightOperand() {
        return rightOperand;
    }

    protected abstract int evaluate(int leftOperand, int rightOperand);

    public int evaluate(int variableValue) {
        return this.evaluate(this.leftOperand.evaluate(variableValue), this.rightOperand.evaluate(variableValue));
    }
}
