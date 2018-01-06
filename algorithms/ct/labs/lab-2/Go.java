/**
 * Nariman Safiulin (woofilee)
 * File: Go.java
 * Created on: Nov 14, 2015
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Go {

    static class Queue {
        static class Pair {
            int key;
            int val;

            Pair(int k, int v) {
                key = k;
                val = v;
            }
        }

        ArrayList<Pair> queue = new ArrayList<>();

        void enqueue(int key) {
            queue.add(new Pair(key, 20));
        }

        void dequeue() {
            queue.remove(0);
        }

        int size() {
            return queue.size();
        }

        int iterate() {
            if (size() > 0)
            {
                Pair temp = queue.get(0);
                temp.val--;
                if (temp.val == 0) {
                    dequeue();
                    return temp.key;
                }
                return 0;
            }
            return 0;
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("saloon.in"));
        PrintWriter pw = new PrintWriter("saloon.out");

        Queue queue = new Queue();
        int times[] = new int[101];
        Queue.Pair enters[] = new Queue.Pair[1441];
        int n = sc.nextInt();
        int temp;

        for (int i = 0; i < 1441; i++) {
            enters[i] = null;
        }

        int t;
        for (int i = 1; i < n + 1; i++) {
            t = sc.nextInt() * 60 + sc.nextInt();
            if (enters[t] == null) {
                enters[t] = new Queue.Pair(i, sc.nextInt());
            } else {
                pw.println("WTF!?!?!?");
                sc.close();
                pw.close();
                return;
            }
        }

        for (int i = 0; i < 1441; i++) {
            temp = queue.iterate();
            if (temp != 0)
                times[temp] = i;
            if (enters[i] != null) {
                if (queue.size() > enters[i].val) {
                    times[enters[i].key] = i;
                } else {
                    queue.enqueue(enters[i].key);
                }
            }
        }

        for (int i = 1; i < n + 1; i++) {
            pw.println(times[i] / 60 + " " + times[i] % 60);
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
