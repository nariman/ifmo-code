/*
 * Nariman Safiulin (woofilee)
 * File: E.java
 * Created on: Jan 12, 2017
 */

import java.io.*;
import java.util.*;
import java.nio.file.*;
import static java.lang.Math.min;

public class E {
    private static final String PROBLEM_NAME = "treeeg";

    private static class MultiSet {
        private final TreeMap<Integer, Integer> map;
        int min;

        MultiSet() {
            map = new TreeMap<>();
            min = (int) 1e9;
        }

        void add(int e) {
            map.put(e, map.getOrDefault(e, 0) + 1);
            min = min(min, e);
        }

        void remove(int e) {
            int m = map.getOrDefault(e, 0);

            if (m == 1) {
                map.remove(e);
                min = map.firstKey();
            } else if (m > 1) {
                map.put(e, m - 1);
            }
        }
    }

    private static class Edge {
        final int u;
        final int l;

        Edge(int u, int l) {
            this.u = u;
            this.l = l;
        }
    }

    private static int n;
    private static ArrayList<Edge>[] graph;
    private static int[] parent;
    private static int[] labels;
    private static boolean[] centroids;
    private static int[] sizes;
    private static int[][] distances;
    private static MultiSet[] nears;
    private static int[] near;

    @SuppressWarnings("unchecked")
    private static void solve(Scanner in, BufferedWriter out) throws Exception {
        n = in.i();
        graph = new ArrayList[n];
        parent = new int[n];
        labels = new int[n];
        centroids = new boolean[n];
        sizes = new int[n];
        distances = new int[n][(int) (Math.log(n) / Math.log(2) + 1)];
        nears = new MultiSet[n];
        near = new int[n];

        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
            parent[i] = -1;
            centroids[i] = false;
            nears[i] = new MultiSet();
            nears[i].add((int) 1e9);
            near[i] = (int) 1e9;
        }

        for (int i = 1; i < n; i++) {
            int u = in.i() - 1;
            int v = in.i() - 1;
            int l = in.i();

            graph[u].add(new Edge(v, l));
            graph[v].add(new Edge(u, l));
        }

        decompose(0, 0);
        for(int i = 0; i < n; i++)
            distances(i, -1, labels[i], 0);

        // Adding 0
        {
            int v = 0, label = labels[0];

            while (v != -1) {
                nears[v].add(distances[0][label]);
                near[v] = nears[v].min;
                v = parent[v];
                label--;
            }
        }

        int q = in.i();
        StringBuilder sb = new StringBuilder();

        while (q --> 0) {
            String type = in.s();
            int u = in.i() - 1;
            int v = u, label = labels[u];

            switch (type) {
                case "+":
                    while (v != -1) {
                        nears[v].add(distances[u][label]);
                        near[v] = nears[v].min;
                        v = parent[v];
                        label--;
                    }
                    break;
                case "-":
                    while (v != -1) {
                        nears[v].remove(distances[u][label]);
                        near[v] = nears[v].min;
                        v = parent[v];
                        label--;
                    }
                    break;
                case "?":
                    int ans = (int) 1e9;

                    while (v != -1) {
                        ans = min(ans, near[v] + distances[u][label]);
                        v = parent[v];
                        label--;
                    }

                    sb.append(ans).append("\n");
                    break;
            }
        }

        out.write(sb.toString());
    }

    private static void distances(int u, int p, int label, int d) {
        distances[u][label] = d;

        for (Edge v : graph[u])
            if (v.u != p && labels[v.u] >= label)
                distances(v.u, u, label, d + v.l);
    }

    private static void dfs(int u, int p) { // u is current node, p is parent node
        sizes[u] = 1;

        for (Edge v : graph[u])
            if (v.u != p && !centroids[v.u]) {
                dfs(v.u, u);
                sizes[u] += sizes[v.u];
            }
    }

    private static int decompose(int u, int depth) {
        dfs(u, -1);
        int nodes = sizes[u] / 2; // number of nodes in current subtree / 2
        int c = u;
        int p = -1;

        outer: while (true) {
            for (Edge v : graph[c])
                if (v.u != p && !centroids[v.u] && sizes[v.u] > nodes) {
                    p = c;
                    c = v.u;
                    continue outer;
                }

            break;
        }

        centroids[c] = true;
        labels[c] = depth;

        for (Edge v : graph[c])
            if (!centroids[v.u])
                parent[decompose(v.u, depth + 1)] = c;

        return c;
    }

    private static void naive(Scanner in, BufferedWriter out) throws Exception {
        // Code naive solution here
    }

    private static void testgen(Scanner in, BufferedWriter out) throws Exception {
        Random random = new Random();

        // Code test generation here
    }

    private static void run(String name, String ie, String oe, Runner f) throws Exception {
        run(name, ie, oe, f, false);
    }

    private static void run(String name, String ie, String oe, Runner f,
                            boolean files) throws Exception {
        if (files) System.out.print(name + " running... ");
        long start = System.nanoTime();

        Scanner in;
        BufferedWriter out;

        if (files) {
            in = new Scanner(new BufferedReader(new FileReader(new File(PROBLEM_NAME + "." + ie))));
            out = new BufferedWriter(new FileWriter(new File(PROBLEM_NAME + "." + oe)));
        } else {
            in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
            out = new BufferedWriter(new OutputStreamWriter(System.out));
        }

        f.run(in, out);

        in.close();
        out.close();

        long end = System.nanoTime();
        if (files) System.out.println("OK, " + (end - start) / 1e6 + " ms.");
    }

    public static void main(String[] args) throws Exception {
        boolean judge = false;
        boolean naive = false;

        int tests = 150;

        boolean files = true | judge; // we cannot use system i/o, if judge mode is on

        String ie = "in";
        String ae = "ans";
        String oe = "out";

        if (!judge) {
            run("Solution", ie, oe, E::solve, files);
        } else {
            for (int i = 0; i < tests; i++) {
                System.out.println("Checking test #" + i + "...");

                System.out.print("  - "); run("Test generation", ie, ie, E::testgen, files);
                if (naive) {
                    System.out.print("  - ");
                    run("Naive solution", ie, ae, E::naive, files);
                }
                System.out.print("  - "); run("Main solution", ie, oe, E::solve, files);

                if (naive) {
                    System.out.print("  - Checking... ");

                    if (Files.readAllLines(Paths.get(PROBLEM_NAME + "." + oe)) ==
                            Files.readAllLines(Paths.get(PROBLEM_NAME + "." + ae))) {
                        System.out.println("OK");
                    } else {
                        System.out.println("WA");
                        return;
                    }
                }

                System.out.println();
            }
        }
    }

    private interface Runner {
        void run(Scanner in, BufferedWriter out) throws Exception;
    }

    private static class Scanner {
        BufferedReader r;
        StringTokenizer t;

        Scanner(BufferedReader r) {
            this.r = r;
            t = new StringTokenizer("");
        }

        void close() throws Exception { r.close(); }
        boolean has() throws Exception {
            while (!t.hasMoreTokens()) {
                String line = r.readLine();
                if (line == null)
                    return false;
                t = new StringTokenizer(line);
            }
            return true;
        }

        String s() throws Exception { return has() ? t.nextToken() : null; }
        int i() throws Exception { return Integer.parseInt(s()); }
        long l() throws Exception { return Long.parseLong(s()); }
        double d() throws Exception { return Double.parseDouble(s()); }
    }
}
