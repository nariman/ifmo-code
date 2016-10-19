/**
* Nariman Safiulin (woofilee)
* File: ArrayStack.java
* Created on: Nov 12, 2015
*/

import java.io.*;
import java.util.*;

public class ArrayStack<E> {
    static <E> E pop(ArrayList<E> queue) {
        return queue.remove(0);
    }

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(new File("stack.in"));
        PrintWriter out = new PrintWriter(new File("stack.out"));

        ArrayList<String> stack1 = new ArrayList<String>();
        ArrayList<Integer> stack2 = new ArrayList<Integer>();

        while (in.hasNext()) {
            String op = in.next();
            if (op.equals("+")) {
                stack1.add(in.next());
            } else if (op.equals("++")) {
                stack2.add(in.nextInt());
            } else if (op.equals("-")) {
                out.println(pop(stack1).toUpperCase());
            } else {
                out.println(pop(stack2));
            }
        }

        in.close();
        out.close();
    }
}
