/*
 * Nariman Safiulin (woofilee)
 * File: I.java
 * Created on: Dec 3, 2016
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

import static java.util.Arrays.fill;

@SuppressWarnings("unchecked")
public class I {
    public static final String PROBLEM_NAME = "multiassignment";

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(new File(PROBLEM_NAME + ".in"));
        PrintWriter out = new PrintWriter(new File(PROBLEM_NAME + ".out"));

        solve(in, out);

        out.close();
        in.close();
    }

    private static class Edge {
        final int u;
        final int v;
        final int c;
        final int w;
        final int index;
        int f;

        Edge(int u, int v, int c, int w, int index) {
            this.u = u;
            this.v = v;
            this.c = c;
            this.w = w;
            this.index = index;
            this.f = 0;
        }
    }

    static private final long MAX = Long.MAX_VALUE - Integer.MAX_VALUE;
    static private final int MAX_N = 100 + 1;

    static private ArrayList<Edge>[] graph;
    private static HashSet<Integer>[] rest;
    private static int[] matching;
    private static boolean[] visited;

    private static void solve(Scanner in, PrintWriter out) throws Exception {
        int n = in.nextInt();
        int factor = (n + 1) * 2;
        int k = in.nextInt();

        graph = new ArrayList[factor];
        rest = new HashSet[n];
        visited = new boolean[n];
        matching = new int[n];
        fill(matching, -1);

        for (int i = 0; i < graph.length; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                edge(i + 1, n + j + 1, 1, in.nextInt());
            }
        }

        for (int i = 0; i < n; i++) {
            edge(0, i + 1, k, 0);
            edge(n + i + 1, factor - 1, k, 0);
        }

        long result = 0;

        while (true) {
            int q[] = new int[MAX_N];
            long d[] = new long[factor];
            int used[] = new int[factor];
            int p[] = new int[factor];
            int index[] = new int[factor];

            fill(d, MAX);
            d[0] = 0;

            int s = 0;
            int t = 1;

            while (s != t) {
                int x = q[s++];
                if (s == MAX_N) s = 0;
                used[x] = 2;
                int i = 0;

                for (Edge cur : graph[x]) {
                    if (cur.f != cur.c && d[x] + cur.w < d[cur.v]) {
                        d[cur.v] = cur.w + d[x];

                        if (used[cur.v] == 0) {
                            q[t++] = cur.v;
                            t = t == MAX_N ? 0 : t;
                        } else if (used[cur.v] == 2) {
                            s = s == 0 ? MAX_N - 1 : s - 1;
                            q[s] = cur.v;
                        }

                        used[cur.v] = 1;
                        p[cur.v] = x;
                        index[cur.v] = i;
                    }
                    i++;
                }
            }

            if (d[factor - 1] == MAX)
                break;

            long f = MAX;
            for (int cur = factor - 1; cur != 0; cur = p[cur]) {
                Edge edge = graph[p[cur]].get(index[cur]);
                if (edge.c - edge.f < f) {
                    f = edge.c - edge.f;
                }
            }

            for (int cur = factor - 1; cur != 0; cur = p[cur]) {
                Edge edge = graph[p[cur]].get(index[cur]);
                edge.f += f;
                graph[cur].get(edge.index).f -= f;
                result += edge.w * f;
            }
        }

        out.println(result);

        for (int i = 1; i <= n; i++) {
            for (Edge e : graph[i]) {
                if (e.f == 1 && e.c == 1) {
                    if (rest[i - 1] == null) rest[i - 1] = new HashSet<>();
                    rest[i - 1].add(e.v - n - 1);
                }
            }
        }

        for (int p = 0; p < k; p++) {
            boolean[] visitedInGraph = new boolean[n];

            for (int i = 0; i < n; i++) {
                if (rest[i] != null) {
                    for (int u : rest[i]) {
                        if (matching[u] == -1) {
                            matching[u] = i;
                            visitedInGraph[i] = true;
                            break;
                        }
                    }
                }
            }

            for (int i = 0; i < n; i++) {
                if (visitedInGraph[i]) continue;
                fill(visited, false);
                res(i);
            }

            int[] rematch = new int[n];
            for (int i = 0; i < rematch.length; i++) {
                rematch[matching[i]] = i;
            }

            for (int i = 0; i < rematch.length; i++) {
                out.print((rematch[i] + 1) + " ");
                rest[i].remove(rematch[i]);
            }

            out.println();
            fill(matching, -1);
        }
    }

    private static void edge(int u, int v, int c, int w) {
        Edge a = new Edge(u, v, c, w,  graph[v].size());
        Edge b = new Edge(v, u, 0, -w, graph[u].size());
        graph[u].add(a);
        graph[v].add(b);
    }

    private static boolean res(int v) {
        if (visited[v]) return false;
        visited[v] = true;

        if (rest[v] != null) {
            for (int u : rest[v]) {
                if (matching[u] == -1 || res(matching[u])) {
                    matching[u] = v;
                    return true;
                }
            }
        }

        return false;
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

