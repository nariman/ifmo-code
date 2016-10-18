/**
 * Nariman Safiulin (woofilee)
 * File: C.java
 * Created on: Nov 14, 2015
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class C {
    static class Stack {
        ArrayList<Integer> stack = new ArrayList<>();

        void push(Integer c) {
            stack.add(c);
        }

        Integer pop() {
            return stack.remove(stack.size() - 1);
        }

        Integer last() {
            return stack.get(stack.size() - 1);
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("postfix.in"));
        PrintWriter pw = new PrintWriter("postfix.out");

        Stack stack = new Stack();
        stack.push(sc.nextInt());
        stack.push(sc.nextInt());
        String c = sc.next();
        while (c != null) {
            switch (c) {
                case "+":
                    stack.push(stack.pop() + stack.pop());
                    break;
                case "-":
                    Integer i1 = stack.pop();
                    Integer i2 = stack.pop();
                    stack.push(i2 - i1);
                    break;
                case "*":
                    stack.push(stack.pop() * stack.pop());
                    break;
                default:
                    stack.push(Integer.valueOf(c));
                    break;
            }
            c = sc.next();
        }

        pw.print(stack.pop());
        pw.close();
        sc.close();
    }

    static class Scanner {
        BufferedReader br;
        StringTokenizer t;

        Scanner(File file) throws Exception {
            br = new BufferedReader(new FileReader(file));
            t = new StringTokenizer("");
        }

        boolean hasNext() throws Exception {
            while (!t.hasMoreTokens()) {
                String line = br.readLine();
                if (line == null)
                    return false;
                t = new StringTokenizer(line);
            }
            return t.hasMoreTokens();
        }

        String next() throws Exception {
            if (hasNext()) {
                return t.nextToken();
            }
            return null;
        }

        int nextInt() throws Exception {
            return Integer.parseInt(next());
        }

        double nextDouble() throws Exception {
            return Double.parseDouble(next());
        }

        void close() throws Exception {
            br.close();
        }
    }
}
