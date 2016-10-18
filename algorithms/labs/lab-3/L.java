/**
 * Nariman Safiulin (woofilee)
 * File: L.java
 * Created on: Jan 05, 2016
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Random;
import java.util.StringTokenizer;

public class L {
    static class DisjointSetForest {
        Random random = new Random();
        int[] data;

        DisjointSetForest(int size) {
            size += 2;
            data = new int[size];
            while (size --> 0) data[size] = size;
        }

        void makeSet(int v) {
            data[v] = v;
        }

        int find(int v) {
            return (data[v] == v) ? v : (data[v] = find(data[v]));
        }

        void unite(int x, int y) {
            if (random.nextInt() % 2 == 0) data[find(x)] = find(y);
            else data[find(y)] = find(x);
        }
    }

    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(new File("cutting.in"));
        PrintWriter pw = new PrintWriter(new File("cutting.out"));
        DisjointSetForest dsf = new DisjointSetForest(sc.nextInt());
        int m = sc.nextInt() * 2, k = sc.nextInt();

        while (m --> 0) {
            sc.next();
        }
        boolean[] b = new boolean[k], ans = new boolean[k];
        int[][] q = new int[k][2];
        int d = 0;

        for (int i = 0; i < k; i++) {
            b[i] = sc.next().equals("ask");
            q[i][0] = sc.nextInt();
            q[i][1] = sc.nextInt();
        }

        while (k --> 0) {
            if (b[k]) ans[d++] = dsf.find(q[k][0]) == dsf.find(q[k][1]);
            else dsf.unite(q[k][0], q[k][1]);
        }

        while (d --> 0) {
            pw.println((ans[d]) ? "YES" : "NO");
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

        long nextLong() throws Exception {
            return Long.parseLong(next());
        }

        double nextDouble() throws Exception {
            return Double.parseDouble(next());
        }

        void close() throws Exception {
            br.close();
        }
    }
}
