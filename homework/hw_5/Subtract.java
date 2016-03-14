/**
 * Nariman Safiulin (woofilee)
 * File: Subtract.java
 * Created on: Mar 14, 2016
 */

public class Subtract extends Operation {
    Subtract(ExpressionObject leftOperand, ExpressionObject rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected int evaluate(int leftOperand, int rightOperand) {
        return leftOperand - rightOperand;
    }
}
