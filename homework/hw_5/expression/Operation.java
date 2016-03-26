/**
 * Nariman Safiulin (woofilee)
 * File: Operation.java
 * Created on: Mar 27, 2016
 */

package expression;

public interface Operation extends ExpressionObject {
    int operate(int left, int right);
    double operate(double left, double right);
}
