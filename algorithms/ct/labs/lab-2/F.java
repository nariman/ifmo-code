/**
 * Nariman Safiulin (woofilee)
 * File: F.java
 * Created on: Nov 14, 2015
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class F {
    static class DoublyLinkedList {
        static class Node {
            Node prev;
            Node next;
            int num;

            Node(Node prev, Node next, int num) {
                this.prev = prev;
                this.next = next;
                this.num = num;
            }
        }

        Node head;
        Node tail;
        Node middle;
        int cnt;

        DoublyLinkedList() {
            head = new Node(null, null, 0);
            tail = new Node(null, null, 0);
            head.prev = tail;
            tail.next = head;
            middle = head;
            cnt = 0;
        }

        void normal(int num) {
            tail.next = new Node(tail, tail.next, num);
            tail.next.next.prev = tail.next;
            cnt++;
            if (cnt % 2 == 1) {
                middle = middle.prev;
            }
        }

        void vip(int num) {
            middle.prev.next = new Node(middle.prev, middle, num);
            middle.prev = middle.prev.next;
            cnt++;
            if (cnt % 2 == 1) {
                middle = middle.prev;
            }
        }

        int toHell() {
            Node t = head.prev;
            t.prev.next = head;
            head.prev = t.prev;
            cnt--;
            if (cnt == 0) {
                middle = head;
            } else if (cnt % 2 == 1) {
                middle = middle.prev;
            }
            return t.num;
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("hospital.in"));
        PrintWriter pw = new PrintWriter("hospital.out");

        int n = sc.nextInt();
        DoublyLinkedList queue = new DoublyLinkedList();
        String c;

        for (int i = 0; i < n; i++) {
            c = sc.next();
            switch (c) {
                case "+":
                    queue.normal(sc.nextInt());
                    break;
                case "*":
                    queue.vip(sc.nextInt());
                    break;
                case "-":
                    pw.println(queue.toHell());
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
