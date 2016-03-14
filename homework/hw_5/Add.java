/**
 * Nariman Safiulin (woofilee)
 * File: Add.java
 * Created on: Mar 14, 2016
 */

public class Add extends Operation {
    Add(ExpressionObject leftOperand, ExpressionObject rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected int evaluate(int leftOperand, int rightOperand) {
        return leftOperand + rightOperand;
    }
}
