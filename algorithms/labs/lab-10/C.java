/*
 * Nariman Safiulin (woofilee)
 * File: C.java
 * Created on: Jan 11, 2017
 */

import java.io.*;
import java.util.*;
import java.nio.file.*;

public class C {
    private static final String PROBLEM_NAME = "decomposition";

    private static int n;
    private static ArrayList<Integer>[] graph;
    private static int[] parent;
    private static boolean[] centroids;
    private static int[] sizes;

    @SuppressWarnings("unchecked")
    private static void solve(Scanner in, BufferedWriter out) throws Exception {
        n = in.i();
        graph = new ArrayList[n];
        parent = new int[n];
        centroids = new boolean[n];
        sizes = new int[n];

        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
            parent[i] = 0;
            centroids[i] = false;
        }

        for (int i = 1; i < n; i++) {
            int u = in.i() - 1;
            int v = in.i() - 1;

            graph[u].add(v);
            graph[v].add(u);
        }

        decompose(0);

        for (int i = 0; i < n; i++)
            out.write(parent[i] + " ");
    }

    private static void dfs(int u, int p) { // u is current node, p is parent node
        sizes[u] = 1;

        for (int v : graph[u])
            if (v != p && !centroids[v]) {
                dfs(v, u);
                sizes[u] += sizes[v];
            }
    }

    private static int decompose(int u) {
        dfs(u, -1);
        int nodes = sizes[u] / 2; // number of nodes in current subtree / 2
        int c = u;
        int p = -1;

        outer: while (true) {
            for (int v : graph[c])
                if (v != p && !centroids[v] && sizes[v] > nodes) {
                    p = c;
                    c = v;
                    continue outer;
                }

            break;
        }

        centroids[c] = true;

        for (int v : graph[c])
            if (!centroids[v])
                parent[decompose(v)] = c + 1;

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
            run("Solution", ie, oe, C::solve, files);
        } else {
            for (int i = 0; i < tests; i++) {
                System.out.println("Checking test #" + i + "...");

                System.out.print("  - "); run("Test generation", ie, ie, C::testgen, files);
                if (naive) {
                    System.out.print("  - ");
                    run("Naive solution", ie, ae, C::naive, files);
                }
                System.out.print("  - "); run("Main solution", ie, oe, C::solve, files);

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
