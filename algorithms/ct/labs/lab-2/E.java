/**
 * Nariman Safiulin (woofilee)
 * File: E.java
 * Created on: Nov 14, 2015
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class E {
    static class DoublyLinkedList {
        static class Node {
            int left;
            int right;

            Node() {
                left = 0;
                right = 0;
            }
        }

        public Node list[];

        DoublyLinkedList(int n) {
            list = new Node[n + 1];
            for (int i = 0; i < n + 1; i++) {
                list[i] = new Node();
            }
        }

        // push left
        void left(int curr, int pivot) {
            if (list[pivot].left != 0) {
                list[list[pivot].left].right = curr;
            }
            list[curr].left = list[pivot].left;
            list[curr].right = pivot;
            list[pivot].left = curr;
        }

        // push right
        void right(int curr, int pivot) {
            if (list[pivot].right != 0) {
                list[list[pivot].right].left = curr;
            }
            list[curr].right = list[pivot].right;
            list[curr].left = pivot;
            list[pivot].right = curr;
        }

        void pop(int curr) {
            if (list[curr].left != 0) {
                list[list[curr].left].right = list[curr].right;
            }
            if (list[curr].right != 0) {
                list[list[curr].right].left = list[curr].left;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("formation.in"));
        PrintWriter pw = new PrintWriter("formation.out");

        int n = sc.nextInt();
        int m = sc.nextInt();
        DoublyLinkedList list = new DoublyLinkedList(n);
        int t;

        for (int i = 0; i < m; i++) {
            String cmd = sc.next();
            switch (cmd) {
                case "left":
                    list.left(sc.nextInt(), sc.nextInt());
                    break;
                case "right":
                    list.right(sc.nextInt(), sc.nextInt());
                    break;
                case "leave":
                    list.pop(sc.nextInt());
                    break;
                case "name":
                    t = sc.nextInt();
                    pw.println(list.list[t].left + " " + list.list[t].right);
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
