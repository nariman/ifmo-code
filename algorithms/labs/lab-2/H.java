/**
 * Nariman Safiulin (woofilee)
 * File: H.java
 * Created on: Nov 14, 2015
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class H {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("hemoglobin.in"));
        PrintWriter pw = new PrintWriter("hemoglobin.out");

        long list[] = new long[50005];
        list[0] = 0;
        int n = sc.nextInt();
        int cnt = 1;

        for (int i = 0; i < n; i++) {
            String op = sc.next();
            switch (op.charAt(0)) {
                case '+':
                    list[cnt] = list[cnt - 1] + Long.parseLong(op.substring(1));
                    cnt++;
                    break;
                case '-':
                    cnt--;
                    pw.println(list[cnt] - list[cnt - 1]);
                    break;
                case '?':
                    pw.println(list[cnt - 1] - list[cnt - Integer.parseInt(op.substring(1)) - 1]);
                    break;
            }
        }
        pw.close();
        sc.close();
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
            return t.hasMoreTokens();
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

        double nextDouble() throws Exception {
            return Double.parseDouble(next());
        }

        void close() throws Exception {
            br.close();
        }
    }
}
