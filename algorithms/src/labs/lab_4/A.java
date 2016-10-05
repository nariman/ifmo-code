/**
 * Nariman Safiulin (woofilee)
 * File: A.java
 * Created on: Mar 1, 2016
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class A {
    public static final String PROBLEM_NAME = "lis";

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(new File(PROBLEM_NAME + ".in"));
        PrintWriter out = new PrintWriter(new File(PROBLEM_NAME + ".out"));

        solve(in, out);

        out.close();
        in.close();
    }

    private static void solve(Scanner in, PrintWriter out) throws Exception {
        int n = in.nextInt();
        int[] arr = new int[n], d = new int[n], s = new int[n];
        int max = -1, pos = -1, prev;

        for (int i = 0; i < n; i++) {
            arr[i] = in.nextInt();
            d[i] = 1;
            s[i] = -1;
            for (int j = 0; j < i; j++) {
                if (arr[j] < arr[i] && d[i] < (1 + d[j])) {
                    d[i] = 1 + d[j];
                    s[i] = j;
                }
            }

            if (max < d[i]) {
                max = d[i];
                pos = i;
            }
        }

        prev = -1;
        while (pos != -1) {
            d[pos] = prev;
            prev = pos;
            pos = s[pos];
        }

        out.println(max);
        pos = prev;
        while (pos != -1) {
            out.print(arr[pos]);
            out.print(" ");
            pos = d[pos];
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
