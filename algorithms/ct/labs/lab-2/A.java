/**
 * Nariman Safiulin (woofilee)
 * File: A.java
 * Created on: Nov 14, 2015
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class A {
    static class Stack {
        ArrayList<Character> stack = new ArrayList<>();

        int size() {
            return stack.size();
        }

        void push(Character c) {
            stack.add(c);
        }

        Character pop() {
            return stack.remove(stack.size() - 1);
        }

        Character last() {
            return stack.get(stack.size() - 1);
        }

        String full() {
            StringBuilder sb = new StringBuilder();
            int len = stack.size();
            for (int i = 0; i < len; i++) {
                sb.append(stack.get(i));
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) throws Exception {
        long current = System.currentTimeMillis();
        Scanner sc = new Scanner(new File("decode.in"));
        PrintWriter pw = new PrintWriter("decode.out");

        String code = sc.next();
        StringBuilder source = new StringBuilder();

        Stack chars = new Stack();
        int len = code.length();
        if (len > 0) {
            chars.push(code.charAt(0));
        }
        int i = 1;

        while (i < len) {
            if (chars.size() == 0 || chars.last() != code.charAt(i)) {
                chars.push(code.charAt(i));
            } else {
                chars.pop();
            }
            i++;
        }

        pw.print(chars.full());
        pw.close();
        sc.close();
        System.out.println(System.currentTimeMillis() - current);
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
