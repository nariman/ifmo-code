/**
 * Nariman Safiulin (woofilee)
 * File: L.java
 * Created on: Mar 20, 2016
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class L {
    public static final String PROBLEM_NAME = "salesman";

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(new File(PROBLEM_NAME + ".in"));
        PrintWriter out = new PrintWriter(new File(PROBLEM_NAME + ".out"));

        solve(in, out);

        out.close();
        in.close();
    }

    private static void solve(Scanner in, PrintWriter out) throws Exception {
        int n = in.nextInt(), m = in.nextInt();
        int[][] w = new int[n][n], d = new int[1 << n][n];


        for (int i = 0; i < n; ++i)
            Arrays.fill(w[i], Integer.MAX_VALUE);
        for (int i = 0; i < 1 << n; ++i)
            Arrays.fill(d[i], Integer.MAX_VALUE);

        for (int i = 0; i < m; i++) {
            int a = in.nextInt() - 1, b = in.nextInt() - 1;
            w[a][b] = w[b][a] = in.nextInt();
        }

        for (int i = 0; i < n; i++) {
            d[1 << i][i] = 0;
        }

        for (int i = 0; i < 1 << n; i++) {
            for (int j = 0; j < n; j++) {
                if (d[i][j] == Integer.MAX_VALUE)
                    continue;
                for (int k = 0; k < n; k++) {
                    int off = 1 << k;
                    if ((i & off) == 0 && w[j][k] != Integer.MAX_VALUE) {
                        d[i | off][k] = Math.min(d[i | off][k], d[i][j] + w[j][k]);
                    }
                }
            }
        }

        int min = Integer.MAX_VALUE;
        int off = d.length - 1;

        for (int i = 0; i < n; i++) {
            min = Math.min(min, d[off][i]);
        }

        out.println((min == Integer.MAX_VALUE) ? -1 : min);
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
