/*
 * Nariman Safiulin (narimansafiulin)
 * File: Main.java
 * Created on: Mar 19, 2018
 */

import java.util.*;

public class U0901Main {
    static class U0901WorkArray<T extends Number> {
        T[] arrNums;

        public U0901WorkArray(T[] numP) {
            arrNums = numP;
        }

        public double sum() {
            double doubleWork = 0;

            for (Number el : this.arrNums) {
                doubleWork = doubleWork + el.doubleValue();
            }

            return doubleWork;
        }
    }

    public static void main(String[] args) {
        //
        // Task #2
        //

        Integer intArr[] = {10, 20, 15};
        Float floatArr[] = {1.1f, 2.2f, 3.3f, 4f};

        U0901WorkArray<Integer> insWorkArrayInt = new U0901WorkArray(intArr);
        U0901WorkArray<Float> insWorkArrayFloat = new U0901WorkArray(floatArr);

        System.out.println(insWorkArrayInt.sum());
        System.out.println(insWorkArrayFloat.sum());
    }
}
