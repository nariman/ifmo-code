/**
 * Nariman Safiulin (woofilee)
 * File: B.java
 * Created on: May 15, 2016
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class B {
    public static final String PROBLEM_NAME = "pathmgep";

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(new File(PROBLEM_NAME + ".in"));
        PrintWriter out = new PrintWriter(new File(PROBLEM_NAME + ".out"));

        solve(in, out);

        out.close();
        in.close();
    }

    private static void solve(Scanner in, PrintWriter out) throws Exception {
        int n = in.nextInt();
        int s = in.nextInt() - 1;
        int f = in.nextInt() - 1;

        if (s == f) {
            out.print(0);
            return;
        }

        int[][] matrix = new int[n][n];
        long[] distances = new long[n];
        boolean[] used = new boolean[n];

        Arrays.fill(distances, Long.MAX_VALUE);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = in.nextInt();
            }
        }

        distances[s] = 0;

        for (int i = 0; i < n; i++) {
            int v = -1;

            for (int j = 0; j < n; j++) {
                if (!used[j] && (v == -1 || distances[j] < distances[v])) {
                    v = j;
                }
            }

            if (distances[v] == Long.MAX_VALUE)
                break;
            used[v] = true;

            for (int j = 0; j < n; j++) {
                if (v != j && matrix[v][j] != -1) {
                    if (distances[v] + matrix[v][j] < distances[j]) {
                        distances[j] = distances[v] + matrix[v][j];
                    }
                }
            }
        }

        out.println(distances[f] == Long.MAX_VALUE ? -1 : distances[f]);
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
