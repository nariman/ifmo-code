/**
 * Nariman Safiulin (woofilee)
 * File: D.java
 * Created on: Nov 14, 2015
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class D {

    static class List {
        static class Node {
            Node prev;
            int min;

            Node(Node prev, int min) {
                this.prev = prev;
                this.min = min;
            }
        }

        Node tail;

        List() {
            tail = new Node(null, Integer.MAX_VALUE);
        }

        void push(int x) {
            tail = new Node(tail, Math.min(x, tail.min));
        }

        void pop() {
            tail = tail.prev;
        }

        int min() {
            return tail.min;
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("stack-min.in"));
        PrintWriter pw = new PrintWriter("stack-min.out");

        List list = new List();
        int n = sc.nextInt();

        for (int i = 0; i < n; i++) {
            int op = sc.nextInt();
            switch (op) {
                case 1:
                    list.push(sc.nextInt());
                    break;
                case 2:
                    list.pop();
                    break;
                case 3:
                    pw.println(list.min());
                    break;
            }
        }
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
            return true;
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
