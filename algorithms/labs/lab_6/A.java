/**
 * Nariman Safiulin (woofilee)
 * File: A.java
 * Created on: May 15, 2016
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class A {
    public static final String PROBLEM_NAME = "pathbge1";

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(new File(PROBLEM_NAME + ".in"));
        PrintWriter out = new PrintWriter(new File(PROBLEM_NAME + ".out"));

        solve(in, out);

        out.close();
        in.close();
    }

    private static void solve(Scanner in, PrintWriter out) throws Exception {
        int n = in.nextInt();
        int m = in.nextInt();
        HashSet<Integer>[] edges = new HashSet[n];
        int[] distances = new int[n];
        boolean[] used = new boolean[n];

        Arrays.fill(distances, Integer.MAX_VALUE);
        for (int i = 0; i < n; i++) {
            edges[i] = new HashSet<>();
        }

        for (int i = 0; i < m; i++) {
            int f = in.nextInt() - 1;
            int s = in.nextInt() - 1;
            edges[f].add(s);
            edges[s].add(f);
        }

        ArrayDeque<Integer> q = new ArrayDeque<>();
        q.addLast(0);
        distances[0] = 0;

        while (!q.isEmpty()) {
            int v = q.removeFirst();
            if (used[v]) continue;
            used[v] = true;
            final int distance = distances[v] + 1;
            edges[v].forEach(i -> {
                distances[i] = Math.min(distances[i], distance);
                q.addLast(i);
            });
        }

        for (int i = 0; i < n; i++) {
            out.print(distances[i] + " ");
        }
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
