/**
 * Nariman Safiulin (woofilee)
 * File: Divide.java
 * Created on: Mar 14, 2016
 */

public class Divide extends Operation {
    Divide(ExpressionObject leftOperand, ExpressionObject rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected int evaluate(int leftOperand, int rightOperand) {
        return leftOperand / rightOperand;
    }
}
