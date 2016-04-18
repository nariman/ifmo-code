/**
 * Nariman Safiulin (woofilee)
 * File: CheckedSqrt.java
 * Created on: Apr 03, 2016
 */

package expression;

public class CheckedSqrt extends CheckedAbstractUnaryOperation {

    public CheckedSqrt(TripleExpression object) {
        super(object);
    }

    @Override
    public String nameSelfOperation() {
        return "CheckedSqrt";
    }

    private void assertSafeOperation(int value) {
        if (value < 0) {
            throw new ArithmeticException("[ERROR] Cannot to get square " +
                    "root of negative value " + value);
        }
    }

    @Override
    protected int operate(int value) {
        assertSafeOperation(value);

//        return (int) Math.sqrt((double) value);

        if (value == 0) {
            return 0;
        }

        int div = value, res = value;

//        if ((value & 4294901760L) > 0)
//            if ((value & 4278190080L) > 0)
//                div = 16383;
//            else
//                div = 1023;
//        else if ((value & 65280) > 0)
//            div = 63;
//        else
//            div = (value > 4) ? 7 : value;

        while (true) {
            div = (value / div + div) >> 1;
            if (res > div) {
                res = div;
            } else {
                return res;
            }
        }
    }
}
