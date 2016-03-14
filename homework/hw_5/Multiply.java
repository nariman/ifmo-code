/**
 * Nariman Safiulin (woofilee)
 * File: Multiply.java
 * Created on: Mar 14, 2016
 */

public class Multiply extends Operation {
    Multiply(ExpressionObject leftOperand, ExpressionObject rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected int evaluate(int leftOperand, int rightOperand) {
        return leftOperand * rightOperand;
    }
}
