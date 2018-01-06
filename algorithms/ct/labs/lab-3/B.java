/**
 * Nariman Safiulin (woofilee)
 * File: B.java
 * Created on: Dec 19, 2015
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class B {
    static class SegmentTree {
        int size;
        long a[];

        void build(long[] source, int n) {
            size = 1;
            while (size < n) {
                size <<= 1;
            }
            //System.out.println("Int = " + n + ". Size = " + size + ". 2Size = " + (size << 1));

            a = new long[size << 1];
            for (int i = size; i < (size << 1); i++) {
                a[i] = 0;
            }
            System.arraycopy(source, 0, a, size, n);

            for (int i = size; i-- > 0; ) {
                a[i] = a[i << 1] + a[(i << 1) + 1];
            }

            /*
            for (int i = 0; i < size << 1; i++) {
                System.out.print(a[i] + " ");
            }
            System.out.println("-");
            */
        }

        private long sumRecursive(int v, int l, int r, int vl, int vr) {
            //System.out.printf("%d %d %d %d %d\n", v, l, r, vl, vr);
            if (l > r) {
                return 0;
            }
            if (l == vl && r == vr) {
                return a[v];
            }
            int t = (vl + vr) >> 1;
            return sumRecursive(v << 1, l, Math.min(r, t), vl, t) +
                    sumRecursive((v << 1) + 1, Math.max(l, t + 1), r, t + 1, vr);
        }

        long sum(int l, int r) {
            return sumRecursive(1, l, r, 1, size);
        }

        void update(int i, int x) {
            i += size - 1;
            a[i] = x;
            while ((i >>= 1) != 0) {
                a[i] = a[i << 1] + a[(i << 1) + 1];
            }

            /*
            for (i = 0; i < size << 1; i++) {
                System.out.print(a[i] + " ");
            }
            System.out.println("-");
            */
        }
    }

    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(new File("rsq.in"));
        PrintWriter pw = new PrintWriter(new File("rsq.out"));

        int n = sc.nextInt();
        long a[] = new long[n + 1];
        SegmentTree st = new SegmentTree();

        for (int i = 0; i < n; i++) {
            a[i] = sc.nextLong();
        }

        st.build(a, n);

        int t1, t2;
        while (sc.hasNext()) {
            switch (sc.next()) {
                case "set":
                    t1 = sc.nextInt();
                    t2 = sc.nextInt();
                    st.update(t1, t2);
                    break;
                case "sum":
                    t1 = sc.nextInt();
                    t2 = sc.nextInt();
                    pw.println(st.sum(t1, t2));
                    break;
            }
        }

        sc.close();
        pw.close();
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
