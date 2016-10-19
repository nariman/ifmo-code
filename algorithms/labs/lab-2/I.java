/**
 * Nariman Safiulin (woofilee)
 * File: I.java
 * Created on: Nov 16, 2015
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class I {
    static class Queue {
        public static class Node {
            Node prev;
            Node next;
            int tickets;

            Node(Node prev, Node next, int tickets) {
                this.prev = prev;
                this.next = next;
                this.tickets = tickets;
            }
        }

        Node head;
        Node tail;
        int cnt;

        Queue() {
            head = new Node(null, null, -1);
            tail = new Node(null, null, -1);
            head.prev = tail;
            tail.next = head;
            cnt = 0;
        }

        Node first() {
            return head.prev;
        }

        void add(int tickets) {
            tail.next = new Node(tail, tail.next, tickets);
            tail.next.next.prev = tail.next;
            cnt++;
        }

        Node move() {
            if (cnt == 1) {
                return first();
            }
            Node first = head.prev;
            head.prev = first.prev;
            first.next = tail.next;
            first.next.prev = first;
            first.prev = tail;
            tail.next = first;
            return first();
        }

        Node pop() {
            head.prev = head.prev.prev;
            cnt--;
            return first();
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("bureaucracy.in"));
        PrintWriter pw = new PrintWriter(new File("bureaucracy.out"));

        Queue queue = new Queue();
        int n = sc.nextInt();
        int m = sc.nextInt();

        for (int i = n; i-- > 0; ) {
            queue.add(sc.nextInt());
        }

        int k = m / n;
        int offset = m % n;
        Queue.Node current = queue.first();

        if (k != 0) {
            for (int i = n; i-- > 0; ) {
                if (current.tickets <= k) {
                    offset += k - current.tickets;
                    current = queue.pop();
                    n--;
                } else {
                    current.tickets -= k;
                    current = queue.move();
                }
            }
        }

        while (n > 0 && offset-- > 0) {
            if (current.tickets == 1) {
                current = queue.pop();
                n--;
            } else {
                current.tickets--;
                current = queue.move();
            }
        }

        if (n == 0) {
            pw.print("-1");
        } else {
            pw.println(n);
            while (n-- > 0) {
                pw.print(current.tickets + " ");
                current = current.prev;
            }
        }

        sc.close();
        pw.close();
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
