/**
 * Nariman Safiulin (woofilee)
 * File: C.java
 * Created on: Nov 8, 2016
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class C {
    public static final String PROBLEM_NAME = "lca_rmq";

    public static void main(String[] args) throws Exception {
        System.out.print("CCCCC");
        Scanner in = new Scanner(new File(PROBLEM_NAME + ".in"));
        PrintWriter out = new PrintWriter(new File(PROBLEM_NAME + ".out"));

        solve(in, out);

        out.close();
        in.close();
    }

    private static int n;
    private static int m;
    private static int logn;
    private static ArrayList<Integer>[] graph;
    private static int[] depth;
    private static int[][] euler;
    private static int[] first;
    private static int[] part;
    private static int t = 0;

    private static void solve(Scanner in, PrintWriter out) throws Exception {
        n = in.nextInt();
        m = in.nextInt();
        logn = (int) (Math.log(2 * n - 1) / Math.log(2.0)) + 1;
        graph = new ArrayList[n];
        depth = new int[n];
        euler = new int[2 * n - 1][logn];
        first = new int[n];
        part = new int[2 * n];

        Arrays.fill(first, -1);

        for (int i = 0; i < n; i++)
            graph[i] = new ArrayList<>();
        for (int i = 1; i < n; i++)
            graph[in.nextInt()].add(i);

        process();

        int u = in.nextInt();
        int v = in.nextInt();
        long x = (in.nextLong() % n);
        long y = (in.nextLong() % n);
        long z = (in.nextLong() % n);

        int a = lca(u, v);
        long result = a;

        for (int i = 2; i <= m; i++) {
            u = (int) ((x * u + y * v + z) % n);
            v = (int) ((x * v + y * u + z) % n);
            a = lca((u + a) % n, v);
            result += a;
        }

        out.write((Long.toString(result)));
    }

    private static int lca(int u, int v) {
        u = first[u];
        v = first[v];

        if (u > v) {
            int t = u;
            u = v;
            v = t;
        }

        int c = part[v - u + 1];
        int a = euler[u][c];
        int b = euler[v + 1 - (1 << c)][c];
        return (depth[a] < depth[b]) ? a : b;
    }

    private static void dfs(int v, int e) {
        euler[t++][0] = v;
        depth[v] = e;
        for (int it : graph[v]) {
            dfs(it, e + 1);
            euler[t++][0] = v;
        }
    }

    private static void process() {
        dfs(0, 0);

        for (int i = 0; i < 2 * n - 1; i++)
            if (first[euler[i][0]] == -1)
                first[euler[i][0]] = i;

        for (int i = 2; i < 2 * n; i++)
            part[i] = part[i / 2] + 1;

        for (int j = 1; j <= logn; j++) {
            for (int i = 1; i + (1 << j) < 2 * n; i++) {
                int u = euler[i][j - 1];
                int v = euler[i + (1 << (j - 1))][j - 1];
                euler[i][j] = (depth[u] < depth[v]) ? u : v;
            }
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
