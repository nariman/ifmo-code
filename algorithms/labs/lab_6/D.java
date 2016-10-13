/**
 * Nariman Safiulin (woofilee)
 * File: D.java
 * Created on: May 15, 2016
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class D {
    public static final String PROBLEM_NAME = "pathbgep";

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(new File(PROBLEM_NAME + ".in"));
        PrintWriter out = new PrintWriter(new File(PROBLEM_NAME + ".out"));

        solve(in, out);

        out.close();
        in.close();
    }

    private static class DirectedEdge implements Comparable<DirectedEdge> {
        int to;
        long weight;

        DirectedEdge(int to, long w) {
            this.to = to;
            this.weight = w;
        }

        @Override
        public int compareTo(DirectedEdge o) {
            return Long.compare(this.weight, o.weight);
        }
    }

    private static void solve(Scanner in, PrintWriter out) throws Exception {
        int n = in.nextInt();
        int m = in.nextInt();
        long[] distances = new long[n];
        ArrayList<DirectedEdge>[] edges = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            edges[i] = new ArrayList<>();
        }

        for (int i = 0; i < m; i++) {
            int f = in.nextInt() - 1;
            int s = in.nextInt() - 1;
            int w = in.nextInt();
            edges[f].add(new DirectedEdge(s, w));
            edges[s].add(new DirectedEdge(f, w));
        }

        Arrays.fill(distances, Long.MAX_VALUE);
        distances[0] = 0;
        PriorityQueue<DirectedEdge> q = new PriorityQueue<>();
        q.add(new DirectedEdge(0, 0));

        while (!q.isEmpty()) {
            int v = q.peek().to;
            if (q.poll().weight > distances[v])
                continue;

            for (DirectedEdge edge : edges[v]) {
                int to = edge.to;
                long w = edge.weight;
                if (distances[v] + w < distances[to]) {
                    distances[to] = distances[v] + w;
                    q.add(new DirectedEdge(to, distances[to]));
                }

            }
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
